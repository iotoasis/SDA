package com.pineone.icbms.sda.comm.logger;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Aspect
public class LoggerAspect {
	private Log log = LogFactory.getLog(this.getClass());
	static String name = "";
	static String type = ""; 

	//@Around("execution(* pineone..controller.*Controller.*(..)) or execution(* pineone..service.*Impl.*(..)) or execution(* pineone..dao.*DAO.*(..))")
	@Around("execution(* *..controller.*Controller.*(..)) or execution(* *..service.*Impl.*(..)) or execution(* *..dao.*DAO.*(..))")
	public Object logPrint(ProceedingJoinPoint joinPoint) throws Throwable {
		type = joinPoint.getSignature().getDeclaringTypeName();
		
		if (type.indexOf("Controller") > -1) {
			name = "Controller  \t:  ";
		}
		else if (type.indexOf("Service") > -1) {
			name = "ServiceImpl  \t:  ";
		}
		else if (type.indexOf("DAO") > -1) {
			name = "DAO  \t\t:  ";
		}
		log.info(name + type + "." + joinPoint.getSignature().getName() + "()");
		return joinPoint.proceed();
	}
}
