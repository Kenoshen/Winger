package com.winger.utils;

/**
 * Used in subscriber/notifier class patterns throughout the Winger framework. This is a generic class that is used to subscribe listeners to a given
 * event. That event will either be a named event ('TRANSITION-START"), or a named event with an expected value ('X-BUTTON', 'DOWN').
 *
 * @author Michael P. Wingfield
 * @copyright Nov 4, 2014
 *
 */
public class SubscriptionRecords
{
    public static <HANDLER, NAME, VALUE> SubscriptionRecord3<HANDLER, NAME, VALUE> subscriptionRecord3(HANDLER handler, NAME name, VALUE val)
    {
        return new SubscriptionRecords.SubscriptionRecord3<HANDLER, NAME, VALUE>(handler, name, val);
    }
    
    
    public static <HANDLER, NAME> SubscriptionRecord2<HANDLER, NAME> subscriptionRecord2(HANDLER handler, NAME name)
    {
        return new SubscriptionRecords.SubscriptionRecord2<HANDLER, NAME>(handler, name);
    }
    
    
    /**
     * Class used by Notifier, should not be used directly for the most part
     * 
     * @param <HANDLER>
     * @param <NAME>
     * @param <VALUE>
     */
    public static class SubscriptionRecord3<HANDLER, NAME, VALUE>
    {
        public HANDLER handler;
        public NAME name;
        public VALUE val;
        
        
        /**
         * Class used by Notifier, should not be used directly for the most part
         * 
         * @param handler
         * @param name
         * @param val
         */
        public SubscriptionRecord3(HANDLER handler, NAME name, VALUE val)
        {
            if (handler == null)
                throw new RuntimeException("Handler for subscriptionRecord3 cannot be null");
            this.handler = handler;
            if (name == null)
                throw new RuntimeException("Name for subscriptionRecord3 cannot be null");
            this.name = name;
            if (val == null)
                throw new RuntimeException("Val for subscriptionRecord3 cannot be null");
            this.val = val;
        }
        
        
        /**
         * Determines if this subscription record's name and val equals the given name and val
         * 
         * @param name
         * @param val
         * @return
         */
        public boolean isQualified(NAME name, VALUE val)
        {
            try
            {
                return (this.name.equals(name) && this.val.equals(val));
            } catch (Exception e)
            {
                throw new RuntimeException(e);
            }
        }
        
        
        @Override
        public boolean equals(Object obj)
        {
            if (obj != null && obj instanceof SubscriptionRecord3<?, ?, ?>)
            {
                SubscriptionRecord3<?, ?, ?> o = (SubscriptionRecord3<?, ?, ?>) obj;
                return (handler.equals(o.handler) && name.equals(o.name) && val.equals(o.val));
            }
            return false;
        }
        
        
        @Override
        public int hashCode()
        {
            return super.hashCode();
        }
    }
    
    
    /**
     * Class used by Notifier, should not be used directly for the most part
     * 
     * @param <HANDLER>
     * @param <NAME>
     */
    public static class SubscriptionRecord2<HANDLER, NAME>
    {
        public HANDLER handler;
        public NAME name;
        
        
        /**
         * Class used by Notifier, should not be used directly for the most part
         * 
         * @param handler
         * @param name
         */
        public SubscriptionRecord2(HANDLER handler, NAME name)
        {
            if (handler == null)
                throw new RuntimeException("Handler for subscriptionRecord2 cannot be null");
            this.handler = handler;
            if (name == null)
                throw new RuntimeException("Name for subscriptionRecord2 cannot be null");
            this.name = name;
        }
        
        
        /**
         * Determines if this subscription record's name equals the given name
         * 
         * @param name
         * @return
         */
        public boolean isQualified(NAME name)
        {
            if (this.name != null)
            {
                return this.name.equals(name);
            } else if (name == null)
            {
                return true;
            }
            return false;
        }
        
        
        @Override
        public boolean equals(Object obj)
        {
            if (obj != null && obj instanceof SubscriptionRecord2<?, ?>)
            {
                SubscriptionRecord2<?, ?> o = (SubscriptionRecord2<?, ?>) obj;
                return (handler.equals(o.handler) && name.equals(o.name));
            }
            return false;
        }
        
        
        @Override
        public int hashCode()
        {
            return super.hashCode();
        }
    }
}
