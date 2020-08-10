package com.panda.redis.base.threadPoolCommon;

import org.apache.log4j.Logger;

import java.util.concurrent.*;

/**
 * 
 * @ClassName:  HeartBeatExecutorService 
 * @Description:心跳线程池服务类
 * @author:     gl.wu
 * @date:       2018-10-8 上午11:58:20
 */
public class HeartBeatExecutorService {
	private static final Logger log = Logger.getLogger(HeartBeatExecutorService.class);
	private int corePoolSize = 20;
	private int maximumPoolSize = 30;
	private long keepAliveTime = 1;
	private int workQueueSize = 0;
	private TimeUnit timeUnit = TimeUnit.SECONDS;
	private BlockingQueue<Runnable> workQueue = null;
	private String name;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public int getCorePoolSize() {
		return corePoolSize;
	}

	public void setCorePoolSize(int corePoolSize) {
		this.corePoolSize = corePoolSize;
	}

	public int getMaximumPoolSize() {
		return maximumPoolSize;
	}

	public void setMaximumPoolSize(int maximumPoolSize) {
		this.maximumPoolSize = maximumPoolSize;
	}

	public long getKeepAliveTime() {
		return keepAliveTime;
	}

	public void setKeepAliveTime(long keepAliveTime) {
		this.keepAliveTime = keepAliveTime;
	}

	public int getWorkQueueSize() {
		return workQueueSize;
	}

	public void setWorkQueueSize(int workQueueSize) {
		this.workQueueSize = workQueueSize;
	}

	public TimeUnit getTimeUnit() {
		return timeUnit;
	}

	public void setTimeUnit(TimeUnit timeUnit) {
		this.timeUnit = timeUnit;
	}

	public BlockingQueue<Runnable> getWorkQueue() {
		return workQueue;
	}

	public void setWorkQueue(BlockingQueue<Runnable> workQueue) {
		this.workQueue = workQueue;
	}

	public ExecutorService create() {
		ExecutorService executorService = null;
		if (maximumPoolSize <= 0) {
			maximumPoolSize = 2 * corePoolSize;
		}
		if (workQueue == null) {
			if (workQueueSize <= 0) {
				workQueue = new ArrayBlockingQueue<Runnable>(maximumPoolSize);
			} else {
				workQueue = new ArrayBlockingQueue<Runnable>(workQueueSize);
			}
		}
		executorService = new ThreadPoolExecutor(corePoolSize,
												maximumPoolSize,
												keepAliveTime, 
												timeUnit, 
												workQueue,
												new NamedThreadFactory(name),
												new CustomRejectedExecutionHandler());
		
		return executorService;
	}
	
	/**
	 * 自定义阻塞型线程池 当池满时会阻塞任务提交
	 * @author gl.wu
	 *
	 */
	private class CustomRejectedExecutionHandler implements
            RejectedExecutionHandler {
		@Override
		public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
			try {
				if (!executor.isShutdown()) {
					log.info("-开启阻塞策略, 当前长度:" + executor.getQueue().size());
					executor.getQueue().put(r);
				}
			} catch (InterruptedException e) {
				log.error("CustomRejectedExecutionHandler | error：",e);
			}
		}
	}
	
}
