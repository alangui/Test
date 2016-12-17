package cn.itcast.nsfw.user.action;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionContext;

import cn.itcast.core.action.BaseAction;
import cn.itcast.core.exception.SysException;
import cn.itcast.core.util.QueryHelper;
import cn.itcast.nsfw.role.service.RoleService;
import cn.itcast.nsfw.user.entity.User;
import cn.itcast.nsfw.user.entity.UserRole;
import cn.itcast.nsfw.user.service.UserService;


public class UserAction extends BaseAction{
	private static final long serialVersionUID = 1L;
	
	@Resource
	private UserService userService;
	@Resource
	private RoleService roleService;
	private User user;
	private File headImg;
	private String headImgContentType;
	private String headImgFileName;
	
	private File userExcel;
	private String userExcelContentType;
	private String userExcelFileName;
	
	private String[] userRoleIds;
	private String strName;
	
	public String listUI() throws SysException{
		QueryHelper queryHelper = new QueryHelper(User.class,"u");
		if(user!=null){
			if(StringUtils.isNotBlank(user.getName())){
				queryHelper.addCondition("u.name like ?", "%"+user.getName()+"%");
			}
		}
		pageResult = userService.getPageResult(queryHelper, getPageNo(), getPageSize());
		return "listUI";
	}
	
	public String addUI(){
		strName = user.getName();
		ActionContext.getContext().getContextMap().put("roleList", roleService.findObjects());
		return "addUI";
	}
	
	public String add(){
		try {
			if(user !=null ){
				if(headImg!=null){
					String filePath = ServletActionContext.getServletContext().getRealPath("upload/user");
					String fileName = UUID.randomUUID().toString().replace("-", "") + headImgFileName.substring(headImgFileName.indexOf("."));
					FileUtils.copyFile(headImg, new File(filePath,fileName));
					user.setHeadImg("user/"+fileName);
				}
				userService.saveUserAndRole(user, userRoleIds);
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
		return "list";
	}
	
	public String editUI(){
		ActionContext.getContext().getContextMap().put("roleList", roleService.findObjects());
		if(user !=null && user.getId() !=null){
			strName = user.getName();
			user = userService.findObjectById(user.getId());
			List<UserRole> list = userService.getUserRolesByUserId(user.getId());
			if( list!=null && list.size()>0){
				userRoleIds = new String[list.size()];
				for(int i=0 ; i<list.size() ; i++){
					userRoleIds[i]=list.get(i).getId().getRole().getRoleId();
				}
			}
		}
		return "editUI";
	}
	
	public String edit(){
		try {
			if(user!=null){
				if(headImg!=null){
					String filePath = ServletActionContext.getServletContext().getRealPath("upload/user");
					String fileName = UUID.randomUUID().toString().replace("-", "") + headImgFileName.substring(headImgFileName.indexOf("."));
					FileUtils.copyFile(headImg, new File(filePath,fileName));
					user.setHeadImg("user/" + fileName);
				}
				userService.updateUserAndRole(user,userRoleIds);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "list";
	}
	
	public String delete(){
		if( user!=null && user.getId() !=null){
			strName = user.getName();
			userService.delete(user.getId());
		}
		return "list";
	}
	
	public String deleteSelected(){
		if(selectedRow != null){
			strName = user.getName();
			for(String id : selectedRow){
				userService.delete(id);
			}
		}
		return "list";
	}

	public void exportExcel(){
		try {
			//1、查找用户列表
			//2、导出
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setContentType("application/x-excel");
			response.setHeader("Content-Disposition", "attachment;filename=" + new String("用户列表.xls".getBytes(), "ISO-8859-1") );
			ServletOutputStream outputStream = response.getOutputStream();
			userService.exportExcel(userService.findObjects(), outputStream);
			if(outputStream != null){
				outputStream.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String importExcel(){
		if(userExcel !=null){
			if(userExcelFileName.matches("^.+\\.(?i)((xls)|xlsx)$")){
				userService.importExcel(userExcel,userExcelFileName);
			}
		}
		return "list";
	}
	
	public void verifyAccount(){
		try {
			if(user!=null && StringUtils.isNotBlank(user.getAccount())){
				List<User> list = userService.findUserByAccountAndId(user.getId(),user.getAccount());
				String strResult = "true";
				if(list!=null && list.size()>0){
					strResult = "false";
				}
				HttpServletResponse reponse = ServletActionContext.getResponse();
				reponse.setContentType("text/html");
				ServletOutputStream outputStream = reponse.getOutputStream();
				outputStream.write(strResult.getBytes());
				outputStream.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public File getHeadImg() {
		return headImg;
	}

	public void setHeadImg(File headImg) {
		this.headImg = headImg;
	}

	public String getHeadImgContentType() {
		return headImgContentType;
	}

	public void setHeadImgContentType(String headImgContentType) {
		this.headImgContentType = headImgContentType;
	}

	public String getHeadImgFileName() {
		return headImgFileName;
	}

	public void setHeadImgFileName(String headImgFileName) {
		this.headImgFileName = headImgFileName;
	}

	public File getUserExcel() {
		return userExcel;
	}

	public void setUserExcel(File userExcel) {
		this.userExcel = userExcel;
	}

	public String getUserExcelContentType() {
		return userExcelContentType;
	}

	public void setUserExcelContentType(String userExcelContentType) {
		this.userExcelContentType = userExcelContentType;
	}

	public String getUserExcelFileName() {
		return userExcelFileName;
	}

	public void setUserExcelFileName(String userExcelFileName) {
		this.userExcelFileName = userExcelFileName;
	}

	public String[] getUserRoleIds() {
		return userRoleIds;
	}

	public void setUserRoleIds(String[] userRoleIds) {
		this.userRoleIds = userRoleIds;
	}

	public String getStrName() {
		return strName;
	}

	public void setStrName(String strName) {
		this.strName = strName;
	}
	
	
}
