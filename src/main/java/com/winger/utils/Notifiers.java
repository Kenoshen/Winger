package com.winger.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Used in subscriber/notifier class patterns throughout the Winger framework. This is a generic class that is used to notify subscribers of a given
 * event. That event will either be a named event ('TRANSITION-START"), or a named event with an expected value ('X-BUTTON', 'DOWN').
 *
 * @author Michael P. Wingfield
 * @copyright Nov 4, 2014
 *
 */
public class Notifiers
{
    /**
     * Creates a new notifyer that uses the type NAME and type VALUE to register subscribers
     * 
     * @return
     */
    public static <HANDLER, NAME, VALUE> Notifier3<HANDLER, NAME, VALUE> notifier3()
    {
        return new Notifiers.Notifier3<HANDLER, NAME, VALUE>();
    }
    
    
    /**
     * Creates a new notifyer that uses the type NAME to register subscribers
     * 
     * @return
     */
    public static <HANDLER, NAME> Notifier2<HANDLER, NAME> notifier2()
    {
        return new Notifiers.Notifier2<HANDLER, NAME>();
    }
    
    
    /**
     * Should have a wrapper around it that is in charge of calling events in the handlers
     * 
     * @param <HANDLER>
     * @param <NAME>
     * @param <VALUE>
     */
    public static class Notifier3<HANDLER, NAME, VALUE>
    {
        private List<SubscriptionRecords.SubscriptionRecord3<HANDLER, NAME, VALUE>> subscribers = new ArrayList<SubscriptionRecords.SubscriptionRecord3<HANDLER, NAME, VALUE>>();
        
        
        /**
         * Subscribes an event handler to a given name and value
         * 
         * @param handler the subscriber to the event
         * @param name the name of the event
         * @param val the value of the event
         */
        public void subscribeToEvent(HANDLER handler, NAME name, VALUE val)
        {
            subscribers.add(SubscriptionRecords.subscriptionRecord3(handler, name, val));
        }
        
        
        /**
         * Unsubscribes an event handler from a given name and value
         * 
         * @param handler the subscriber to the event
         * @param name the name of the event
         * @param val the value of the event
         */
        public void UnsubscribeFromEvent(HANDLER handler, NAME name, VALUE val)
        {
            subscribers.remove(SubscriptionRecords.subscriptionRecord3(handler, name, val));
        }
        
        
        /**
         * Gets the SubscriptionRecords of the subscribers who qualify for a given event name with a given value
         * 
         * @param name the name of the event
         * @param val the value of the event
         * @return list of subscribers who qualify for the event
         */
        public List<SubscriptionRecords.SubscriptionRecord3<HANDLER, NAME, VALUE>> getSubscribersToNotify(NAME name, VALUE val)
        {
            List<SubscriptionRecords.SubscriptionRecord3<HANDLER, NAME, VALUE>> subsToNotify = new ArrayList<SubscriptionRecords.SubscriptionRecord3<HANDLER, NAME, VALUE>>();
            for (SubscriptionRecords.SubscriptionRecord3<HANDLER, NAME, VALUE> subscriber : subscribers)
            {
                if (subscriber.isQualified(name, val))
                {
                    subsToNotify.add(subscriber);
                }
            }
            return subsToNotify;
        }
        
        
        /**
         * Checks if there are any subscribers
         * 
         * @return true if there are no subscribers
         */
        public boolean isEmpty()
        {
            return (subscribers.size() == 0);
        }
    }
    
    
    /**
     * Should have a wrapper around it that is in charge of calling events in the handlers
     * 
     * @param <HANDLER>
     * @param <NAME>
     */
    public static class Notifier2<HANDLER, NAME>
    {
        private List<SubscriptionRecords.SubscriptionRecord2<HANDLER, NAME>> subscribers = new ArrayList<SubscriptionRecords.SubscriptionRecord2<HANDLER, NAME>>();
        
        
        /**
         * Subscribes an event handler to a given name
         * 
         * @param handler the subscriber to the event
         * @param name the name of the event
         */
        public void subscribeToEvent(HANDLER handler, NAME name)
        {
            subscribers.add(SubscriptionRecords.subscriptionRecord2(handler, name));
        }
        
        
        /**
         * Unsubscribes an event handler from a given name
         * 
         * @param handler the subscriber to the event
         * @param name the name of the event
         */
        public void unsubscribeFromEvent(HANDLER handler, NAME name)
        {
            subscribers.remove(SubscriptionRecords.subscriptionRecord2(handler, name));
        }
        
        
        /**
         * Gets the SubscriptionRecords of the subscribers who qualify for a given event name
         * 
         * @param name the name of the event
         * @return list of subscribers who qualify for the event
         */
        public List<SubscriptionRecords.SubscriptionRecord2<HANDLER, NAME>> getSubscribersToNotify(NAME name)
        {
            List<SubscriptionRecords.SubscriptionRecord2<HANDLER, NAME>> subsToNotify = new ArrayList<SubscriptionRecords.SubscriptionRecord2<HANDLER, NAME>>();
            for (SubscriptionRecords.SubscriptionRecord2<HANDLER, NAME> subscriber : subscribers)
            {
                if (subscriber.isQualified(name))
                {
                    subsToNotify.add(subscriber);
                }
            }
            return subsToNotify;
        }
        
        
        /**
         * Checks if there are any subscribers
         * 
         * @return true if there are no subscribers
         */
        public boolean isEmpty()
        {
            return (subscribers.size() == 0);
        }
    }
}
