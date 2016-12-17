package cn.itcast.core.exception;

public class ServiceException extends SysException {
	private static final long serialVersionUID = 1L;

	public ServiceException(){
		super("业务操作异常！");
	}
	
	public ServiceException(String message){
		super(message);
	}
}
