package cn.tonghao.remex.business.core.util;

import cn.tonghao.remex.business.core.log.RemexLogger;
import org.slf4j.Logger;

import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;


/**
 * 线程池util
 *
 */
public class ThreadPoolUtil 
{
	
	public static final int THREAD_POOL_CORE_SIZE = 10 * 4;
	
	public static final int THREAD_POOL_BLOCKED_QUEUE_SIZE = 1000;
	
	private static final Logger logger = RemexLogger.getLogger(ThreadPoolUtil.class);

	/**
	 * map管理线程池
	 */
	private final Map<String, ThreadPoolExecutor> threadPoolMap = new ConcurrentHashMap<String, ThreadPoolExecutor>();
	
	/**
	 * 阻塞线程
	 */
	private final ConcurrentHashMap<String, AtomicLong> rejectMap = new ConcurrentHashMap<String, AtomicLong>();
	
	/**
	 * 实例
	 */
	private static final ThreadPoolUtil instance = new ThreadPoolUtil();
	
	private ThreadPoolUtil()
	{
	}

	private ThreadPoolExecutor getThreadPoolExecutor(String name, int poolSize,int queueSize) 
	{
		ThreadPoolExecutor threadPoolExecutor = threadPoolMap.get(name);
		if (threadPoolExecutor == null) 
		{
			synchronized (this) 
			{
				threadPoolExecutor = threadPoolMap.get(name);
				if (threadPoolExecutor == null) 
				{
					threadPoolExecutor = createThreadPoolExecutor(name,poolSize, queueSize);
					threadPoolMap.put(name, threadPoolExecutor);
					rejectMap.putIfAbsent(name, new AtomicLong(0));
				}
			}
		}

		return threadPoolExecutor;
	}

	private ThreadPoolExecutor getThreadPoolExecutor(String name) 
	{
		return getThreadPoolExecutor(name, THREAD_POOL_CORE_SIZE,THREAD_POOL_BLOCKED_QUEUE_SIZE);

	}

	private ThreadPoolExecutor getThreadPoolExecutorTask(String name,int queueSize) {
		return getThreadPoolExecutor(name, THREAD_POOL_CORE_SIZE,queueSize);

	}
	
	private ThreadPoolExecutor createThreadPoolExecutor(String name,int poolSize, int queueSize) 
	{
		if(queueSize<=0){
			queueSize=THREAD_POOL_BLOCKED_QUEUE_SIZE;
		}
		return new ThreadPoolExecutor(poolSize, poolSize * 2, 0L,
				TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(
				queueSize));
	}

	public static void execute(String name, Runnable task) 
	{
		try 
		{
			ThreadPoolExecutor threadPoolExecutor = instance.getThreadPoolExecutor(name);
			threadPoolExecutor.execute(task);
		} 
		catch (Exception e) 
		{
			logger.error("线程池{}执行异常:{}",name,e);
			instance.rejectMap.get(name).incrementAndGet();
		}
	}

	public static void executeTask(String name,Runnable task) {
		try {
			ThreadPoolExecutor threadPoolExecutor = instance.getThreadPoolExecutor(name);
			threadPoolExecutor.execute(task);
		} catch (RejectedExecutionException e) {
			logger.error("线程池{}执行异常:{}",name,e);
			instance.rejectMap.get(name).incrementAndGet();
			throw e;
		} catch (NullPointerException  e){
			logger.error("线程池{}执行抛空指针:{}",name,e);
		}
	}

	public static void executeTimeTask(String name, int queueSize,Runnable task) {
		try {
			ThreadPoolExecutor threadPoolExecutor = instance.getThreadPoolExecutorTask(name,queueSize);
			threadPoolExecutor.execute(task);
		} catch (RejectedExecutionException e) {
			logger.error("线程池{}执行异常:{}",name,e);
			instance.rejectMap.get(name).incrementAndGet();
			throw e;
		} catch (NullPointerException  e){
			logger.error("线程池{}执行抛空指针:{}",name,e);
		}
	}

	public static void execute(String name, int poolSize, int queueSize, Runnable task) 
	{
		try 
		{
			ThreadPoolExecutor threadPoolExecutor = instance.getThreadPoolExecutor(name,poolSize, queueSize);
			threadPoolExecutor.execute(task);
		} 
		catch (Exception e) 
		{
			instance.rejectMap.get(name).incrementAndGet();
		}
	}

	public static <T> Future<T> submit(String name, Callable<T> task) 
	{
		try 
		{
			ThreadPoolExecutor threadPoolExecutor = instance.getThreadPoolExecutor(name);
			return threadPoolExecutor.submit(task);
		} 
		catch (Exception e) 
		{
			instance.rejectMap.get(name).incrementAndGet();
		}

		return null;
	}

	public static <T> Future<T> submit(String name, int poolSize, int queueSize,Callable<T> task) 
	{
		try 
		{
			ThreadPoolExecutor threadPoolExecutor = instance.getThreadPoolExecutor(name,poolSize, queueSize);
			return threadPoolExecutor.submit(task);
		} 
		catch (Exception e) 
		{
			instance.rejectMap.get(name).incrementAndGet();
		}

		return null;
	}
	
	/**
	 * 输出日志
	 * 
	 * @return
	 */
	public static String outPutRejectLog()
	{
		StringBuffer buffer = new StringBuffer();
		buffer.append("队列阻塞数：");
		for(Map.Entry<String, AtomicLong> loop:instance.rejectMap.entrySet())
		{
			buffer.append("name：");
			buffer.append(loop.getKey());
			buffer.append(",数量：");
			buffer.append(loop.getValue());
			buffer.append("|");
		}
		return buffer.deleteCharAt(buffer.length() - 1).toString();
	}

}