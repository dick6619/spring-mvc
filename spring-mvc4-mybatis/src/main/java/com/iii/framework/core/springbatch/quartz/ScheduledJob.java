package com.iii.framework.core.springbatch.quartz;

import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.JobLocator;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.quartz.QuartzJobBean;
//
//import com.iii.emp.domain.PrintMessege;

public class ScheduledJob extends QuartzJobBean {

	static final String JOB_NAME = "jobID";
	
	private JobLocator jobLocator;

	private JobLauncher jobLauncher;

	public void setJobLocator(JobLocator jobLocator) {
		this.jobLocator = jobLocator;
	}

	public void setJobLauncher(JobLauncher jobLauncher) {
		this.jobLauncher = jobLauncher;
	}

	// get params from jobDataAsMap property
	private JobParameters getJobParametersFromJobMap(Map<String, Object> jobDataMap) {

		JobParametersBuilder builder = new JobParametersBuilder();

		for (Entry<String, Object> entry : jobDataMap.entrySet()) {
			String key = entry.getKey();
			Object value = entry.getValue();
			if (value instanceof String && !key.equals(JOB_NAME)) {
				builder.addString(key, (String) value);
			} else if (value instanceof Float || value instanceof Double) {
				builder.addDouble(key, ((Number) value).doubleValue());
			} else if (value instanceof Integer || value instanceof Long) {
				builder.addLong(key, ((Number) value).longValue());
			} else if (value instanceof Date) {
				builder.addDate(key, (Date) value);
			} else {
				// JobDataMap contains values which are not job parameters
				// (ignoring)
			}
		}

		// need unique job parameter to rerun the same job
		builder.addDate("run date", new Date());

		return builder.toJobParameters();

	}

	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		
		// TODO Auto-generated method stub
		Map<String, Object> jobDataMap = context.getMergedJobDataMap();

		String jobName = (String) jobDataMap.get(JOB_NAME);

		JobParameters jobParameters = getJobParametersFromJobMap(jobDataMap);
		
		try {
			jobLauncher.run(jobLocator.getJob(jobName), jobParameters);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
