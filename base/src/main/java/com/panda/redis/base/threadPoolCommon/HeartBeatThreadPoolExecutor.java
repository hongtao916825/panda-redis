package com.panda.redis.base.threadPoolCommon;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import com.panda.redis.base.function.CallBack;
/**
 * 
 * @author GuanglongW
 *
 */
public class HeartBeatThreadPoolExecutor {
	private static ExecutorService executorService = null;
	
	static {
		HeartBeatExecutorService m = new HeartBeatExecutorService();
		m.setName("heartbeat-pool");
		executorService = m.create();
	}

	private HeartBeatThreadPoolExecutor() {
	}

	public static void execute(Runnable runnable) {
		executorService.execute(runnable);
	}

	public static Future<String> submit(CallBack<String> callback) {
		return executorService.submit(callback::apply);
	}
}
