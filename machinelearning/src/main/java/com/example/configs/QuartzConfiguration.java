package com.example.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;

/**
 * Klasa konfigurująca cykliczne wykonywania Cron Taska.
 */
@Configuration
@ComponentScan("Quartz")
public class QuartzConfiguration {

	/**
	 * Ziarno wybierające funkcję do uruchomienia.
	 * @return Ziarno zadania.
     */
	@Bean
	public MethodInvokingJobDetailFactoryBean methodInvokingJobDetailFactoryBean() {
		MethodInvokingJobDetailFactoryBean obj = new MethodInvokingJobDetailFactoryBean();
		obj.setTargetBeanName("algorithmExecutor");
		obj.setTargetMethod("lookForPendingTask");
		return obj;
	}

	/**
	 * Ziarno ustawiające trigger uruchamiający MethodInvokingJobDetailFactoryBean
	 * @return Ziarno triggera.
     */
	@Bean
	public SimpleTriggerFactoryBean simpleTriggerFactoryBean(){
		SimpleTriggerFactoryBean stFactory = new SimpleTriggerFactoryBean();
		stFactory.setJobDetail(methodInvokingJobDetailFactoryBean().getObject());
		stFactory.setStartDelay(3000);
		stFactory.setRepeatInterval(50000);//50 sekund
		return stFactory;
	}

	/**
	 * Ziarno wstawiające simpleTriggerFactoryBean do schedulera.
	 * @return Ziarno schedulera.
     */
	@Bean
	public SchedulerFactoryBean schedulerFactoryBean() {
		SchedulerFactoryBean scheduler = new SchedulerFactoryBean();
		scheduler.setTriggers(simpleTriggerFactoryBean().getObject());
		return scheduler;
	}
}  
 