package com.winger.struct;

import java.util.ArrayList;
import java.util.List;

/**
 * Tuple objects to allow for methods to return multiple pieces of data in a single object
 *
 * @author Michael P. Wingfield
 * @copyright Nov 4, 2014
 *
 */
public class Tups
{
    /**
     * Gets a tuple with one type of many arguments
     * 
     * @param args
     * @return
     */
    public static <T> Tup<T> tup(@SuppressWarnings("unchecked") T... args)
    {
        return new Tups.Tup<T>(args);
    }
    
    
    /**
     * Gets a tuple with two items
     * 
     * @param item1
     * @param item2
     * @return
     */
    public static <T1, T2> Tup2<T1, T2> tup2(T1 item1, T2 item2)
    {
        return new Tups.Tup2<T1, T2>(item1, item2);
    }
    
    
    /**
     * Gets a tuple with three items
     * 
     * @param item1
     * @param item2
     * @param item3
     * @return
     */
    public static <T1, T2, T3> Tup3<T1, T2, T3> tup3(T1 item1, T2 item2, T3 item3)
    {
        return new Tups.Tup3<T1, T2, T3>(item1, item2, item3);
    }
    
    
    /**
     * Gets a tuple with four items
     * 
     * @param item1
     * @param item2
     * @param item3
     * @param item4
     * @return
     */
    public static <T1, T2, T3, T4> Tup4<T1, T2, T3, T4> tup4(T1 item1, T2 item2, T3 item3, T4 item4)
    {
        return new Tups.Tup4<T1, T2, T3, T4>(item1, item2, item3, item4);
    }
    
    
    public static class Tup<T>
    {
        private List<T> collection = new ArrayList<T>();
        
        
        public Tup(@SuppressWarnings("unchecked") T... args)
        {
            if (args != null)
            {
                for (int i = 0; i < args.length; i++)
                {
                    collection.add(args[i]);
                }
            }
        }
        
        
        /**
         * Get the tuple item at the given index (will wrap around)
         * 
         * @param i
         * @return
         */
        public T get(int i)
        {
            if (collection.size() > 0)
            {
                i = i % collection.size();
                if (i < 0)
                {
                    i = collection.size() + i;
                }
                return collection.get(i);
            } else
            {
                return null;
            }
        }
    }
    
    
    public static class Tup2<T1, T2>
    {
        protected static final int count2 = 2;
        
        protected T1 item1;
        protected T2 item2;


        public Tup2(T1 item1, T2 item2) {
            this.item1 = item1;
            this.item2 = item2;
        }
        
        /**
         * Get the first item of the tuple
         *
         * @return
         */
        public T1 i1()
        {
            return item1;
        }
        
        /**
         * Get the second item of the tuple
         *
         * @return
         */
        public T2 i2()
        {
            return item2;
        }
        
        /**
         * Get the tuple item at the given index (will wrap around)
         * 
         * @param i
         * @return
         */
        public Object get(int i)
        {
            i = i % count2;
            if (i < 0)
            {
                i = count2 + i;
            }
            switch (i)
            {
                case 0:
                    return item1;
                case 1:
                    return item2;
                default:
                    return item1;
            }
        }
    }
    
    
    public static class Tup3<T1, T2, T3>
    {
        protected static final int count3 = 3;
        
        protected T1 item1;
        protected T2 item2;
        protected T3 item3;


        public Tup3(T1 i
                    et the third item of the tuple
                            *
                            *@return
                            */
                            public T3 i3() {
            return item3;
        }

        /**
         * Gtem1, T2 item2, T3 item3)
         {
         this.item1 = item1;
         this.item2 = item2;
         this.item3 = item3;
         }


         /**
         * G
         et the first item of the tuple
         *
         * @return
         */
        public T1 i1()
        {
            return item1;
        }

        /**
         * G
         et the second item of the tuple
         *
         * @return
         */
        public T2 i2()
        {
            return item2;
        }

        /**
         * G
         et the tuple item at the given index (will wrap around)
         * 
         * @param i
         * @return
         */
        public Object get(int i)
        {
            i = i % count3;
            if (i < 0)
            {
                i = count3 + i;
            }
            switch (i)
            {
                case 0:
                    return item1;
                case 1:
                    return item2;
                case 2:
                    return item3;
                default:
                    return item1;
            }
        }
    }
    
    
    public static class Tup4<T1, T2, T3, T4>
    {
        protected static final int count4 = 4;
        
        protected T1 item1;
        protected T2 item2;
        protected T3 item3;
        protected T4 item4;


        public Tup4(T1 i
                    et the forth item of the tuple
                            *
                            *@return
                            */
                            public T4 i4() {
            return item4;
        }

        /**
         * Gtem1, T2 item2, T3 item3, T4 item4)
         {
         this.item1 = item1;
         this.item2 = item2;
         this.item3 = item3;
         this.item4 = item4;
         }


         /**
         * G
         et the first item of the tuple
         *
         * @return
         */
        public T1 i1()
        {
            return item1;
        }

        /**
         * G
         et the second item of the tuple
         *
         * @return
         */
        public T2 i2()
        {
            return item2;
        }

        /**
         * G
         et the third item of the tuple
         *
         * @return
         */
        public T3 i3()
        {
            return item3;
        }

        /**
         * G
         et the tuple item at the given index (will wrap around)
         * 
         * @param i
         * @return
         */
        public Object get(int i)
        {
            i = i % count4;
            if (i < 0)
            {
                i = count4 + i;
            }
            switch (i)
            {
                case 0:
                    return item1;
                case 1:
                    return item2;
                case 2:
                    return item3;
                case 3:
                    return item4;
                default:
                    return item1;
            }
        }
    }
}