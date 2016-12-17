package cn.itcast.nsfw.info.action;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionContext;

import cn.itcast.core.action.BaseAction;
import cn.itcast.core.page.PageResult;
import cn.itcast.core.util.QueryHelper;
import cn.itcast.nsfw.info.entity.Info;
import cn.itcast.nsfw.info.service.InfoService;

public class InfoAction extends BaseAction {
	private static final long serialVersionUID = 1L;

	@Resource
	private InfoService infoService;
	private Info info;
	private String strTitle;
	
	public String listUI(){
		ActionContext.getContext().getContextMap().put("infoTypeMap", Info.INFO_TYPE_MAP);
		QueryHelper queryHelper = new QueryHelper(Info.class,"i");
		if(info!=null){
			if(StringUtils.isNotBlank(info.getTitle())){
				queryHelper.addCondition("i.title like ?", "%"+info.getTitle()+"%");
			}
		}
		queryHelper.addOrderByProperty("i.createTime", QueryHelper.ORDER_BY_DESC);
		pageResult = infoService.getPageResult(queryHelper, getPageNo(), getPageSize());
		return "listUI";
	}
	
	public String addUI(){
		strTitle = info.getTitle();
		ActionContext.getContext().getContextMap().put("infoTypeMap", Info.INFO_TYPE_MAP);
		info = new Info();
		info.setCreateTime(new Timestamp(new Date().getTime()));
		return "addUI";
	}

	public String add(){
		if(info!=null){
			infoService.save(info);
		}
		return "list";
	}
	
	public String editUI(){
		ActionContext.getContext().getContextMap().put("infoTypeMap", Info.INFO_TYPE_MAP);
		if(info!=null && info.getInfoId()!=null){
			strTitle = info.getTitle();
			info = infoService.findObjectById(info.getInfoId());
		}
		return "editUI";
	}
	
	public String edit(){
		if(info!=null){
			infoService.update(info);
		}
		return "list";
	}
	
	public String delete(){
		if(info != null && info.getInfoId()!=null){
			strTitle = info.getTitle();
			infoService.delete(info.getInfoId());
		}
		return "list";
	}
	
	public String deleteSelected(){
		if(selectedRow !=null){
			strTitle = info.getTitle();
			for(String id : selectedRow){
				infoService.delete(id);
			}
		}
		return "list";
	}

	public void publicInfo(){
		try {
			if(info!=null){
				Info tem = infoService.findObjectById(info.getInfoId());
				tem.setState(info.getState());
				infoService.update(tem);
				
				HttpServletResponse response = ServletActionContext.getResponse();
				response.setContentType("text/html");
				ServletOutputStream outputStream = response.getOutputStream();
				outputStream.write("更新状态成功".getBytes("utf-8"));
				outputStream.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Info getInfo() {
		return info;
	}

	public void setInfo(Info info) {
		this.info = info;
	}

	public String getStrTitle() {
		return strTitle;
	}

	public void setStrTitle(String strTitle) {
		this.strTitle = strTitle;
	}

	

}
