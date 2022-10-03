/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sleepingteacherassistantos;
import java.util.concurrent.Semaphore;

/**
 *
 * @author Zeko
 */
public class TeachingAssistant implements Runnable{
    // Semaphore used to wakeup TA.
    private SignalSemaphore wakeup;

    // Semaphore used to wait in chairs outside office.
    private Semaphore chairs;

    // Mutex lock (binary semaphore) used to determine if TA is available.
    private Semaphore TeacherAvailable;

    // A reference to the current thread.
    private Thread t;
    private int numberOfTeacher;
    private int numberofchairs;
    public TeachingAssistant(SignalSemaphore w, Semaphore c, Semaphore a,int numberOfTeacher,int numberofchairs)
    {
        t = Thread.currentThread();
        wakeup = w;
        chairs = c;
        TeacherAvailable = a;
        this.numberOfTeacher = numberOfTeacher;
        this.numberofchairs=numberofchairs;
    }

    
    
    @Override
    public void run()
    {
        while (true)
        {
            try
            {
                System.out.println("No students left.  The TA "+numberOfTeacher+ " is going to nap.");
                wakeup.release();
                System.out.println("The TA "+numberOfTeacher+ " was awoke by a student.");
                
                t.sleep(5000);
                
                // If there are other students waiting.
                if (chairs.availablePermits() != numberofchairs)
                {
                    do
                    {
                        t.sleep(5000);
                        chairs.release();
                    }
                    while (chairs.availablePermits() != numberofchairs);                   
                }
            }
            catch (InterruptedException e)
            {
                // Something bad happened.
                continue;
            }
        }
    }
}
