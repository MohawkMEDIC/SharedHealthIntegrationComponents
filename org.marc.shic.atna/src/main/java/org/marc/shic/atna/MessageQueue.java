/**
 * Copyright 2013 Mohawk College of Applied Arts and Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 *
 * @author tylerg
 * Date: Sept 4, 2013
 *
 */
package org.marc.shic.atna;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.apache.log4j.Logger;

/**
 *
 * @author tylerg
 */
public class MessageQueue<T extends Runnable>
{

    private static final Logger LOGGER = Logger.getLogger(MessageQueue.class.getName());
    private BlockingQueue<Runnable> messages;
    private ExecutorService executorService;

    public MessageQueue(int threads)
    {
        this.messages = new LinkedBlockingQueue<Runnable>();
        this.executorService = new ThreadPoolExecutor(threads, threads, 1, TimeUnit.MINUTES, this.messages);
    }

    /**
     * Get the number of messages currently in the queue.
     *
     * @return The number of messages in the queue.
     */
    public synchronized int size()
    {
        return this.messages.size();
    }

    /**
     * Submit a message to the queue.
     *
     * @param message The message to submit.
     */
    public synchronized void enqueueMessage(T message)
    {
        this.executorService.submit(message);
    }

    /**
     * Shutdown the queue.
     */
    public synchronized void shutdown()
    {
        this.executorService.shutdown();
    }

    /**
     * Forces the queue to shutdown.
     *
     * @return List of the remaining queued messages.
     */
    public synchronized List<?> shutdownNow()
    {
        return this.executorService.shutdownNow();
    }

    /**
     * Shutdown the queue and block until the shutdown is complete.
     *
     * @param timeout Number of seconds to timeout after.
     */
    public synchronized void awaitShutdown(int timeout)
    {
        try
        {
         //   this.executorService.shutdown();
            this.executorService.awaitTermination(timeout, TimeUnit.SECONDS);
        } catch (InterruptedException ex)
        {
            LOGGER.error(ex);
        }
    }
}
