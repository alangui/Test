package cn.itcast.nsfw.complain.action;

import java.net.URLDecoder;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionContext;

import cn.itcast.core.action.BaseAction;
import cn.itcast.core.util.QueryHelper;
import cn.itcast.nsfw.complain.entity.Complain;
import cn.itcast.nsfw.complain.entity.ComplainReply;
import cn.itcast.nsfw.complain.service.ComplainService;

public class ComplainAction extends BaseAction {
	private static final long serialVersionUID = 1L;

	@Resource
	private ComplainService complainService;
	private Complain complain;
	private String startTime;
	private String endTime;
	private ComplainReply reply;
	
	private Map<String, Object> statisticMap;
	
	public String listUI(){
		ActionContext.getContext().getContextMap().put("complainStateMap", Complain.COMPLAIN_STATE_MAP);
		try {
			QueryHelper queryHelper = new QueryHelper(Complain.class, "c");
			if(StringUtils.isNotBlank(startTime)){
				startTime = URLDecoder.decode(startTime, "utf-8");
				queryHelper.addCondition("c.compTime>=?", DateUtils.parseDate(startTime+":00", "yyyy-MM-dd HH:mm:ss"));
			}
			if(StringUtils.isNotBlank(endTime)){
				endTime = URLDecoder.decode(endTime, "utf-8");
				queryHelper.addCondition("c.compTime<=?", DateUtils.parseDate(endTime+":59", "yyyy-MM-dd HH:mm:ss"));
			}
			if(complain != null){
				if(StringUtils.isNotBlank(complain.getState())){
					queryHelper.addCondition("c.state=?", complain.getState());
				}
				if(StringUtils.isNotBlank(complain.getCompTitle())){
					complain.setCompTitle(URLDecoder.decode(complain.getCompTitle(),"utf-8"));
					queryHelper.addCondition("c.compTitle like ?", "%" + complain.getCompTitle() + "%");
				}
			}
			queryHelper.addOrderByProperty("c.state", QueryHelper.ORDER_BY_ASC);
			queryHelper.addOrderByProperty("c.compTime", QueryHelper.ORDER_BY_ASC);
			
			pageResult = complainService.getPageResult(queryHelper, getPageNo(), getPageSize());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "listUI";
	}
	
	public String dealUI(){
		ActionContext.getContext().getContextMap().put("complainStateMap", Complain.COMPLAIN_STATE_MAP);
		if(complain!=null){
			complain = complainService.findObjectById(complain.getCompId());
		}
		return "dealUI";
	}

	public String deal(){
		if(complain!=null){
			Complain tem = complainService.findObjectById(complain.getCompId());
			if(!Complain.COMPLAIN_SATTE_DONE.equals(tem.getState())){
				tem.setState(Complain.COMPLAIN_SATTE_DONE);
			}
			if(reply!=null){
				reply.setComplain(tem);
				reply.setReplyTime(new Timestamp(new Date().getTime()));
				tem.getComplainReplies().add(reply);
			}
			complainService.update(tem);
		}
		return "list";
	}
	
	public String getAnnualStatisticData(){
		HttpServletRequest request = ServletActionContext.getRequest();
		int year =0;
		if(request.getParameter("year")!=null){
			year = Integer.valueOf(request.getParameter("year"));
		}else{
			year = Calendar.getInstance().get(Calendar.YEAR);
		}
		statisticMap = new HashMap<String, Object>();
		statisticMap.put("msg", "success");
		statisticMap.put("charData", complainService.getAnnualStatisticDataByYear(year));
		return "annualStatisticData";
	}
	
	public String annualStatisticChartUI(){
		return "annualStatisticChartUI";
	}
	
	public Complain getComplain() {
		return complain;
	}

	public void setComplain(Complain complain) {
		this.complain = complain;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public ComplainReply getReply() {
		return reply;
	}

	public void setReply(ComplainReply reply) {
		this.reply = reply;
	}

	public Map<String, Object> getStatisticMap() {
		return statisticMap;
	}

	public void setStatisticMap(Map<String, Object> statisticMap) {
		this.statisticMap = statisticMap;
	}

}
