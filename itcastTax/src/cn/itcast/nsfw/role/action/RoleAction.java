package cn.itcast.nsfw.role.action;

import java.util.HashSet;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;

import com.opensymphony.xwork2.ActionContext;

import cn.itcast.core.action.BaseAction;
import cn.itcast.core.constant.Constant;
import cn.itcast.core.util.QueryHelper;
import cn.itcast.nsfw.role.entity.Role;
import cn.itcast.nsfw.role.entity.RolePrivilege;
import cn.itcast.nsfw.role.entity.RolePrivilegeId;
import cn.itcast.nsfw.role.service.RoleService;

public class RoleAction extends BaseAction{
	private static final long serialVersionUID = 1L;
	@Resource
	private RoleService roleService;
	private Role role;
	private String[] privilegeIds;
	private String strName;
	
	public String listUI(){
		ActionContext.getContext().getContextMap().put("privilegeMap", Constant.PRIVILEGE_MAP);
		QueryHelper queryHelper = new QueryHelper(Role.class,"r");
		if(role!=null){
			if(StringUtils.isNotBlank(role.getName())){
				queryHelper.addCondition("r.name like ?", "%"+role.getName()+"%");
			}
		}
		pageResult = roleService.getPageResult(queryHelper, getPageNo(), getPageSize());
		return "listUI";
	}
	
	public String addUI(){
		strName = role.getName();
		ActionContext.getContext().getContextMap().put("privilegeMap", Constant.PRIVILEGE_MAP);
		role = null;
		return "addUI";
	}

	public String add(){
		if(role!=null){
			if(privilegeIds!=null){
				HashSet<RolePrivilege> set = new HashSet<RolePrivilege>();
				for(int i=0;i<privilegeIds.length;i++){
					set.add(new RolePrivilege(new RolePrivilegeId(role,privilegeIds[i])));
				}
				role.setRolePrivileges(set);
			}
			roleService.save(role);
		}
		return "list";
	}
	
	public String editUI(){
		strName = role.getName();
		ActionContext.getContext().getContextMap().put("privilegeMap", Constant.PRIVILEGE_MAP);
		if(role!=null && role.getRoleId()!=null){
			role = roleService.findObjectById(role.getRoleId());
			if(role.getRolePrivileges()!=null){
				privilegeIds = new String[role.getRolePrivileges().size()];
				int i=0;
				for(RolePrivilege rp : role.getRolePrivileges()){
					privilegeIds[i++] = rp.getId().getCode();
				}
			}
		}
		return "editUI";
	}
	
	public String edit(){
		if(role!=null){
			if(privilegeIds!=null){
				HashSet<RolePrivilege> set = new HashSet<RolePrivilege>();
				for(int i=0;i<privilegeIds.length;i++){
					set.add(new RolePrivilege(new RolePrivilegeId(role,privilegeIds[i])));
				}
				role.setRolePrivileges(set);
			}
			roleService.update(role);
		}
		return "list";
	}
	
	public String delete(){
		if(role != null && role.getRoleId()!=null){
			roleService.delete(role.getRoleId());
		}
		return "list";
	}
	
	public String deleteSelected(){
		if(selectedRow !=null){
			for(String id : selectedRow){
				roleService.delete(id);
			}
		}
		return "list";
	}
	
	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public String[] getPrivilegeIds() {
		return privilegeIds;
	}

	public void setPrivilegeIds(String[] privilegeIds) {
		this.privilegeIds = privilegeIds;
	}

	public String getStrName() {
		return strName;
	}

	public void setStrName(String strName) {
		this.strName = strName;
	}
	
}
