package servlets;

import java.util.ArrayList;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import newtest.Scheduler;

public class ServletContext implements ServletContextListener{
	Scheduler scheduler = new Scheduler();
    
    public void contextInitialized(ServletContextEvent contextEvent) {
    	//scheduler.googleSBScheduler();
    	//scheduler.updateAlexaNewSites();
    	scheduler.startScheduler();
    }
    public void contextDestroyed(ServletContextEvent contextEvent) {
        System.out.println("Context Destroyed");
    }
}