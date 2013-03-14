/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kakscalcdaemon;

import java.util.HashMap;
import java.util.Iterator;

/**
 *
 * @author nekoko
 * 
 * 
 */
public class Wrapper{

    //thread container
    private static HashMap<Integer, Job> jobs = new HashMap<>();

    public static void wrap(Job job){
        Calculation calculation = new Calculation();
        calculation.init(TaskManager.get(job.getTask()));
        Thread thread = new Thread(calculation);
        thread.start();
        job.setCalculation(calculation);
        job.setThread(thread);
        jobs.put(job.getId(), job);        
    }
    
    public static void stop(Job job){
        jobs.get(job.getId()).getThread().interrupt();
        while (!jobs.get(job.getId()).getThread().isInterrupted()){
            //wait until interrupted
        }
        jobs.get(job.getId()).getCalculation().finish();//interrupted job may not call finish, ensure to call finish
    }
    
    public static void remove(Job job){
        jobs.remove(job.getId());
    }
    
    public static void removeFinished(){
        for(Iterator<Integer> it = jobs.keySet().iterator(); it.hasNext();){
            int job_id = it.next();
            if (jobs.get(job_id).getCalculation().isFinished()){
                jobs.remove(job_id);
            }
        }
    }
    
    
    public static int count(){
        return jobs.size();
    }
    
    public static int cpuUsed(){
        int cpu_used = 0;
        for (Iterator<Integer> it = jobs.keySet().iterator(); it.hasNext(); ){
            cpu_used += jobs.get(it.next()).getReqCPU();
        }
        return cpu_used;
    }
    
    public static int memUsed(){
        int mem_used = 0;
        for (Iterator<Integer> it = jobs.keySet().iterator(); it.hasNext(); ){
            mem_used += jobs.get(it.next()).getReqMem();
        }
        return mem_used;
    }
}
