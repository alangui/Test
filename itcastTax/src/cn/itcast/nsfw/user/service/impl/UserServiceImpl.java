package cn.itcast.nsfw.user.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import cn.itcast.core.service.impl.BaseServiceImpl;
import cn.itcast.core.util.ExcelUtil;
import cn.itcast.nsfw.role.entity.Role;
import cn.itcast.nsfw.user.dao.UserDao;
import cn.itcast.nsfw.user.entity.User;
import cn.itcast.nsfw.user.entity.UserRole;
import cn.itcast.nsfw.user.entity.UserRoleId;
import cn.itcast.nsfw.user.service.UserService;


@Service("userService")
public class UserServiceImpl extends BaseServiceImpl<User> implements UserService {

	
	private UserDao userDao;
	@Resource
	public void setUserDao(UserDao userDao) {
		super.setBaseDao(userDao);
		this.userDao = userDao;
	}

	@Override
	public void delete(Serializable id) {
		this.userDao.delete(id);
		userDao.deleteUserRoleByUserId(id);
	}

	@Override
	public void exportExcel(List<User> userList, ServletOutputStream outputStream) {
		ExcelUtil.exportUserExcel(userList, outputStream);
	}

	@Override
	public void importExcel(File userExcel, String userExcelFileName) {
		try {
			FileInputStream inputStream = new FileInputStream(userExcel);
			boolean is03Excel = userExcelFileName.matches("^.+\\.(?i)(xls)$");
			Workbook workbook = is03Excel ? new HSSFWorkbook(inputStream) : new XSSFWorkbook(inputStream);
			Sheet sheet = workbook.getSheetAt(0);
			
			if(sheet.getPhysicalNumberOfRows()>2){
				User user = null;
				for(int i=2;i<sheet.getPhysicalNumberOfRows();i++){
					Row row = sheet.getRow(i);
					user = new User();
					user.setName(row.getCell(0).getStringCellValue());
					user.setAccount(row.getCell(1).getStringCellValue());
					user.setDept(row.getCell(2).getStringCellValue());
					user.setGender(row.getCell(3).getStringCellValue().equals('男'));
					String mobile = "";
					Cell cell4 = row.getCell(4);
					try{
						mobile = cell4.getStringCellValue();
					}catch(Exception e){
						double dMobile = cell4.getNumericCellValue();
						mobile = BigDecimal.valueOf(dMobile).toString();
					}
					user.setMobile(mobile);
					user.setEmail(row.getCell(5).getStringCellValue());
					Cell cell6 = row.getCell(6);
					if(cell6.getDateCellValue()!=null){
						user.setBirthday(cell6.getDateCellValue());
					}
					user.setPassword("123456");
					user.setState(User.USER_STATE_VALID);
					save(user);
				}
			}
			workbook.close();
			inputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<User> findUserByAccountAndId(String id, String account) {
		return userDao.findUserByAccountAndId(id,account);
	}

	@Override
	public void saveUserAndRole(User user, String... roleIds) {
		save(user);
		if(roleIds!=null){
			for(String roleId : roleIds){
				userDao.saveUserRole(new UserRole(new UserRoleId(new Role(roleId),user.getId())));
			}
		}
	}

	@Override
	public List<UserRole> getUserRolesByUserId(String userId) {
		return userDao.getUserRolesByUserId(userId);
	}

	@Override
	public void updateUserAndRole(User user, String... roleIds) {
		userDao.deleteUserRoleByUserId(user.getId());
		update(user);
		if(roleIds!=null){
			for(String roleId : roleIds){
				userDao.saveUserRole(new UserRole(new UserRoleId(new Role(roleId),user.getId())));
			}
		}
	}

	@Override
	public List<User> findUserByAccountAndPass(String account, String password) {
		return userDao.findUsersByAccountAndPass(account,password);
	}

}
