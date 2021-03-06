package ru.naumen.lecture.tooling;

public class MakeDeadlockCommand implements IToolTesterCommand
{

    @Override
    public String getName()
    {
        return "Сделай deadlock";
    }

    @Override
    public State getTargetState()
    {
        return State.DEADLOCK;
    }

    @Override
    public void execute()
    {
        Object lock1 = new Object();
        Object lock2 = new Object();
        Object lock3 = ToolTesterUtils.getCommonLock();

        Thread blockingThread1 = new BlockingThread("BlockingThread1", lock1, lock2);
        Thread blockingThread2 = new BlockingThread("BlockingThread2", lock2, lock3);
        Thread blockingThread3 = new BlockingThread("BlockingThread3", lock3, lock1);

        blockingThread1.start();
        ToolTesterUtils.doSomeWork(ToolTesterUtils.DEFAULT_WORK_TIME);
        blockingThread2.start();
        ToolTesterUtils.doSomeWork(ToolTesterUtils.DEFAULT_WORK_TIME);
        blockingThread3.start();
    }
}
