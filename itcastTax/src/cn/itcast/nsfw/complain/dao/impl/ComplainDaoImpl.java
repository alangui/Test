package cn.itcast.nsfw.complain.dao.impl;

import java.util.List;

import org.hibernate.SQLQuery;

import cn.itcast.core.dao.impl.BaseDaoImpl;
import cn.itcast.nsfw.complain.dao.ComplainDao;
import cn.itcast.nsfw.complain.entity.Complain;

public class ComplainDaoImpl extends BaseDaoImpl<Complain> implements
		ComplainDao {

	@Override
	public List<Object[]> getAnnualStatisticDataByYear(int year) {
		StringBuffer buf = new StringBuffer();
		buf.append("select imonth,count(comp_id)")
		.append(" from tmonth left join complain on imonth = month(comp_time)")
		.append(" and year(comp_time)=?")
		.append(" group by imonth")
		.append(" order by imonth");
		SQLQuery sql = getSession().createSQLQuery(buf.toString());
		sql.setParameter(0, year);
		return sql.list();
	}

}
