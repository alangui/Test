package cn.itcast.nsfw.complain.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.itcast.core.service.impl.BaseServiceImpl;
import cn.itcast.core.util.QueryHelper;
import cn.itcast.nsfw.complain.dao.ComplainDao;
import cn.itcast.nsfw.complain.entity.Complain;
import cn.itcast.nsfw.complain.service.ComplainService;

@Service("complainService")
public class ComplainServiceImpl extends BaseServiceImpl<Complain> implements
		ComplainService {

	private ComplainDao complainDao;
	@Resource
	public void setComplainDao(ComplainDao complainDao){
		super.setBaseDao(complainDao);
		this.complainDao = complainDao;
	}
	
	@Override
	public void autoDeal(){
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MONTH, cal.get(Calendar.MONTH)+1);
		cal.set(Calendar.DAY_OF_MONTH,1);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		
		QueryHelper query = new QueryHelper(Complain.class,"c");
		query.addCondition("c.state = ?", Complain.COMPLAIN_STATE_UNDONE);
		query.addCondition("c.compTime < ?", cal.getTime());
		
		List<Complain> list = findObjects(query);
		if( list!=null && list.size()>0 ){
			for(int i=0;i<list.size();i++){
				list.get(i).setState(Complain.COMPLAIN_STATE_INVALID);
				update(list.get(i));
			}
		}
	}

	@Override
	public List<Map> getAnnualStatisticDataByYear(int year) {
		List<Map> resList = new ArrayList<Map>();
		List<Object[]> list = complainDao.getAnnualStatisticDataByYear(year);
		Calendar cal = Calendar.getInstance();
		int curYear = cal.get(Calendar.YEAR);
		int curMonth = cal.get(Calendar.MONTH)+1;
		int tempMonth = 0;
		Map<String, Object> map = null;
		for(Object[] obj:list){
			tempMonth = Integer.valueOf(obj[0]+"");
			map = new HashMap<String, Object>();
			map.put("lable", tempMonth+" 月");
			if(curYear == year){
				if(tempMonth>curMonth){
					map.put("value", "");
				}else{
					map.put("value", obj[1] == null ? "0" : obj[1]);
				}
			}else{
				map.put("value", obj[1] == null ? "0" : obj[1]);
			}
			resList.add(map);
		}
		return resList;
	}
}
