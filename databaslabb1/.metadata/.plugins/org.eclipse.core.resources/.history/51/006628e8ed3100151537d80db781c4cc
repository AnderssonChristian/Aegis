package servlets;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import newtest.Scheduler;

public class ServletContext implements ServletContextListener{
	Scheduler scheduler = new Scheduler();
    ServletContext context;
    
    public void contextInitialized(ServletContextEvent contextEvent) {
    	//scheduler.googleSBScheduler();
    }
    public void contextDestroyed(ServletContextEvent contextEvent) {
        System.out.println("Context Destroyed");
    }
}