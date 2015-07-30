package com.winger.struct;

import java.util.ArrayList;
import java.util.List;

/**
 * A free form tree structure with helpful printing function
 *
 * @author Michael P. Wingfield
 * @copyright Nov 4, 2014
 *
 * @param <T>
 */
public class Tree<T>
{
    protected Tree<T> parent = null;
    protected T self;
    protected List<Tree<T>> children = new ArrayList<Tree<T>>();
    
    protected String verticalLine = "|";
    protected String horizontalLine = "_";
    protected String diagonalLine = "\\";
    protected int width = 5;
    
    protected int infiniteLoopSafety = 100;
    
    
    /**
     * A basic tree structure. Each tree object has a parent(Tree<T>) and a list of children(List<Tree<T>>) and a self(T).
     * 
     * @param self
     */
    public Tree(T self)
    {
        setSelf(self);
    }
    
    
    /**
     * Checks if the tree object is a trunk. (no parent)
     * 
     * @return
     */
    public boolean isTrunk()
    {
        return (parent == null);
    }
    
    
    /**
     * Checks if the tree object is a branch. (has parent and children)
     * 
     * @return
     */
    public boolean isBranch()
    {
        return (parent != null && children.size() > 0);
    }
    
    
    /**
     * Checks if the tree object is a leaf. (has parent and no children)
     * 
     * @return
     */
    public boolean isLeaf()
    {
        return (parent != null && children.size() == 0);
    }
    
    
    /**
     * Checks if two tree objects have equal selfs.
     * 
     * @param tree
     * @return
     */
    public boolean isSelfEqual(Tree<T> tree)
    {
        if (tree != null)
        {
            if (self != null)
            {
                return (self.equals(tree.self));
            } else
            {
                return (tree.self == null);
            }
        }
        return false;
    }
    
    /**
     * Gets the number of children tree objets from this tree object.
     *
     * @return
     */
    public int getChildCount()
    {
        return children.size();
    }
    
    /**
     * Gets the total number of leaf tree objects from this tree object and from this tree object's children recursively.
     *
     * @return
     */
    public int getTreeLeafCount()
    {
        if (isLeaf())
        {
            return 1;
        } else
        {
            return this.recurseToFindLeafCount(this, 0, getDepth());
        }
    }
    
    /**
     * Gets the total number of branch tree objects from this tree object and from this tree object's children recursively.
     *
     * @return
     */
    public int getTreeBranchCount()
    {
        if (isLeaf())
        {
            return 0;
        } else
        {
            return this.recurseToFindBranchCount(this, 0, getDepth());
        }
    }
    
    /**
     * Gets the parent of this tree object.
     *
     * @return
     */
    public Tree<T> getParent()
    {
        return parent;
    }

    protected void setParent(Tree<T> parent) {
        this.parent = parent;
    }
    
    /**
     * Gets the self of this tree object.
     * 
     * @return
     */
    public T getSelf()
    {
        return self;
    }

    /**
     * Sets the self of this tree
     *
     * @param self
     */
    public void setSelf(T self) {
        this.self = self;
    }
    
    /**
     * Gets a child of this tree object at a given index.
     *
     * @param index
     * @return
     */
    public Tree<T> getChild(int index)
    {
        return children.get(index);
    }
    
    /**
     * Tries to find the given self that matches with the self of a child and returns the matching child, or null if there is no match.
     *
     * @param otherSelf
     * @return
     */
    public Tree<T> getChildBySelf(T otherSelf)
    {
        for (int i = 0; i < getChildCount(); i++)
        {
            if (getChild(i).getSelf().equals(otherSelf))
            {
                return getChild(i);
            }
        }

        return null;
    }
    
    /**
     * Gets the list of children tree objects from this tree object.
     *
     * @return
     */
    public List<Tree<T>> getChildren()
    {
        return children;
    }
    
    /**
     * Gets a list of all the selfs of each child tree object.
     *
     * @return
     */
    public List<T> getChildrenSelfs()
    {
        List<T> selfs = new ArrayList<T>();
        for (int i = 0; i < getChildCount(); i++)
        {
            selfs.add(getChild(i).getSelf());
        }
        return selfs;
    }
    
    /**
     * Gets the current depth of this tree object. Or in other words, it gets the number of steps to get from this tree object to the trunk tree
     * object.
     *
     * @return
     */
    public int getDepth()
    {
        if (isTrunk())
        {
            return 0;
        } else
        {
            return recurseToFindDepth(getParent(), 0);
        }
    }
    
    /**
     * Gets a list of objects in order from the trunk of the tree to this tree objects parent. Returns an empty list if this tree object is the trunk.
     *
     * @return
     */
    public List<Tree<T>> getAncestry()
    {
        if (isTrunk())
        {
            return new ArrayList<Tree<T>>();
        } else
        {
            return recurseToFindAncestry(getParent(), new ArrayList<Tree<T>>(), 0);
        }
    }
    
    /**
     * Calls the toString() method on each object in the list from the getAncestry() method as well as this tree object itself and concatenates a
     * string using a given string separator.
     *
     * @return <param name="separator"></param>
     */
    public String getPath(String separator)
    {
        StringBuilder sb = new StringBuilder();
        List<Tree<T>> ancestry = getAncestry();
        for (int i = 0; i < ancestry.size(); i++)
        {
            sb.append(ancestry.get(i).getSelf().toString());
            sb.append(separator);
        }
        sb.append(getSelf().toString());
        return sb.toString();
    }
    
    /**
     * Get the leaf objects from this tree object down.
     *
     * @return
     */
    public List<Tree<T>> getLeaves()
    {
        List<Tree<T>> leaves = new ArrayList<Tree<T>>();
        if (isLeaf())
        {
            leaves.add(this);
        } else
        {
            recurseToFindLeaves(this, leaves, 0);
        }
        return leaves;
    }
    
    /**
     * Gets a list of all the tree objects in a breadth first order
     *
     * @return
     */
    public List<T> getBreadthFirstList()
    {
        return recurseToBreadthFirst(this);
    }
    
    /**
     * Gets a list of all the tree objects in a depth first order
     *
     * @return
     */
    public List<T> getDepthFirstList()
    {
        List<T> objs = new ArrayList<T>();
        return recurseToDepthFirst(this, objs, 0);
    }
    
    /**
     * Gets a list of the tree objects in the tree
     *
     * @return
     */
    public List<Tree<T>> getTreeList()
    {
        List<Tree<T>> objs = new ArrayList<Tree<T>>();
        return recurseForTreeList(this, objs, 0);
    }
    
    /**
     * Adds a list of objects where each consecutive tree object is a child of the previous tree object and returns the last object as a tree.
     *
     * @return <param name="branch"></param>
     */
    public Tree<T> addBranch(@SuppressWarnings("unchecked") T... branch)
    {
        Tree<T> currentTree = this;
        for (int i = 0; i < branch.length; i++)
        {
            Tree<T> nextTree = currentTree.addLeaf(branch[i]);
            currentTree = nextTree;
        }
        return currentTree;
    }
    
    /**
     * Adds a list of objects where each consecutive tree object is a child of the previous tree object and returns the last object as a tree.
     *
     * @return <param name="branch"></param>
     */
    public Tree<T> addBranch(@SuppressWarnings("unchecked") Tree<T>... branch)
    {
        Tree<T> currentTree = this;
        for (int i = 0; i < branch.length; i++)
        {
            Tree<T> nextTree = currentTree.addLeaf(branch[i]);
            currentTree = nextTree;
        }
        return currentTree;
    }
    
    /**
     * Adds a list of objects where each consecutive tree object is a child of the previous tree object and returns the last object as a tree.
     *
     * @return <param name="branch"></param>
     */
    public Tree<T> addBranch(List<T> branch)
    {
        Tree<T> currentTree = this;
        for (int i = 0; i < branch.size(); i++)
        {
            Tree<T> nextTree = currentTree.addLeaf(branch.get(i));
            currentTree = nextTree;
        }
        return currentTree;
    }
    
    /**
     * Adds a single object to the list of children.
     *
     * @return <param name="leaf"></param>
     */
    public Tree<T> addLeaf(T leaf)
    {
        return addLeaf(new Tree<T>(leaf));
    }
    
    /**
     * Adds a single object to the list of children.
     *
     * @return <param name="leaf"></param>
     */
    public Tree<T> addLeaf(Tree<T> leaf)
    {
        for (int i = 0; i < getChildCount(); i++)
        {
            if (getChild(i).isSelfEqual(leaf))
            {
                return getChild(i);
            }
        }
        leaf.setParent(this);
        children.add(leaf);
        return leaf;
    }
    
    /**
     * Removes the tree from its parent.getChildren() list.
     *
     * @return public void removeFromTreeStructure() { if (!isTrunk()) { List<Tree<T>> siblings = parent.getChildren(); int selfIndex = -1; for (int i
     *         = 0; i < siblings.size(); i++) { if (siblings.get(i).isSelfEqual(this)) { selfIndex = i; break; } } if (selfIndex >= 0) {
     *         parent.removeChild(selfIndex); setParent(null); } } }
     *
     *         /** Removes a child from this tree objects list of children.
     * @return <param name="index"></param>
     */
    public Tree<T> removeChild(int index)
    {
        Tree<T> child = getChild(index);
        children.remove(index);
        return child;
    }
    
    /**
     * Customize the look of the vertical line for the ToString() method.
     *
     * @return <param name="s"></param> public void customizeVerticalLine(String s) { verticalLine = s; }
     *
     *         /** Customize the look of the horizontal line for the ToString() method.
     * @return <param name="s"></param> public void customizeHorizontalLine(String s) { horizontalLine = s; }
     *
     *         /** Customize the look of the diagonal line for the ToString() method.
     * @return <param name="s"></param> public void customizeDiagonalLine(String s) { diagonalLine = s; }
     *
     *         /** Customize the number of characters in the columns of each line of the // ToString() method.
     * @return <param name="w"></param> public void customizeWidth(int w) { if (w < 2) { width = 2; } else { width = w; } }
     *
     *         /**
     *
     * @return
     */
    @Override
    public int hashCode()
    {
        int prime = 31;
        int result = 1;
        result = prime * result + ((children == null) ? 0 : children.hashCode());
        result = prime * result + ((parent == null) ? 0 : parent.hashCode());
        result = prime * result + ((self == null) ? 0 : self.hashCode());
        return result;
    }
    
    /**
     * Checks if the entire tree structure and it's immediate children are equal to each other.
     *
     * @return <param name="obj"></param>
     */
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (!(obj instanceof Tree<?>))
        {
            return false;
        }
        Tree<?> other = (Tree<?>) obj;
        if (children == null)
        {
            if (other.children != null)
            {
                return false;
            }
        } else if (!children.equals(other.children))
        {
            return false;
        }
        if (parent == null)
        {
            if (other.parent != null)
            {
                return false;
            }
        } else if (!parent.equals(other.parent))
        {
            return false;
        }
        if (self == null)
        {
            if (other.self != null)
            {
                return false;
            }
        } else if (!self.equals(other.self))
        {
            return false;
        }
        return true;
    }
    
    /**
     * Outputs the objects in the tree to a formatted string.
     *
     * @return
     */
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder("\n");
        List<Boolean> lastChildList = new ArrayList<Boolean>();
        lastChildList.add(false);
        recurseToPrintString(this, sb, 0, lastChildList);
        return sb.toString();
    }
    
    protected int recurseToFindDepth(Tree<T> tree, int currentDepth)
    {
        if (tree == null || currentDepth >= infiniteLoopSafety)
        {
            return currentDepth;
        } else
        {
            currentDepth++;
            return recurseToFindDepth(tree.getParent(), currentDepth);
        }
    }
    
    
    protected int recurseToFindLeafCount(Tree<T> tree, int currentLeafCount, int currentDepth)
    {
        currentDepth++;
        if (currentDepth >= infiniteLoopSafety)
        {
            return currentLeafCount;
        } else if (tree.isLeaf())
        {
            return currentLeafCount + 1;
        } else
        {
            for (int i = 0; i < tree.getChildCount(); i++)
            {
                currentLeafCount = recurseToFindLeafCount(tree.getChild(i), currentLeafCount, currentDepth);
            }
            return currentLeafCount;
        }
    }
    
    
    protected void recurseToFindLeaves(Tree<T> tree, List<Tree<T>> leaves, int currentDepth)
    {
        currentDepth++;
        if (currentDepth >= infiniteLoopSafety)
        {
            return;
        } else if (tree.isLeaf())
        {
            leaves.add(tree);
        } else
        {
            for (int i = 0; i < tree.getChildCount(); i++)
            {
                recurseToFindLeaves(tree.getChild(i), leaves, currentDepth);
            }
        }
    }
    
    
    protected int recurseToFindBranchCount(Tree<T> tree, int currentBranchCount, int currentDepth)
    {
        currentDepth++;
        if (tree.isLeaf() || currentDepth >= infiniteLoopSafety)
        {
            return currentBranchCount;
        } else
        {
            currentBranchCount++;
            for (int i = 0; i < tree.getChildCount(); i++)
            {
                currentBranchCount = recurseToFindLeafCount(tree.getChild(i), currentBranchCount, currentDepth);
            }
            return currentBranchCount;
        }
    }
    
    
    protected List<Tree<T>> recurseToFindAncestry(Tree<T> tree, List<Tree<T>> ancestry, int currentDepth)
    {
        currentDepth++;
        if (currentDepth >= infiniteLoopSafety)
        {
            return ancestry;
        } else
        {
            ancestry.add(0, tree);
            if (!tree.isTrunk())
            {
                return recurseToFindAncestry(tree.getParent(), ancestry, currentDepth);
            } else
            {
                return ancestry;
            }
        }
    }
    
    
    protected void recurseToPrintString(Tree<T> tree, StringBuilder sb, int currentDepth, List<Boolean> lastChildList)
    {
        if (currentDepth >= infiniteLoopSafety)
        {
            return;
        }
        
        List<Boolean> newLastChildList = new ArrayList<Boolean>();
        for (int i = 0; i < lastChildList.size(); i++)
        {
            newLastChildList.add(lastChildList.get(i));
        }
        
        for (int i = 0; i < currentDepth; i++)
        {
            if (i + 1 >= currentDepth)
            {
                if (newLastChildList.get(currentDepth))
                {
                    sb.append(diagonalLine);
                    for (int k = 0; k < width - 2; k++)
                    {
                        sb.append(horizontalLine);
                    }
                    sb.append(horizontalLine);
                    // sb.append("`--->");
                    // sb.append("\\____");
                } else
                {
                    sb.append(verticalLine);
                    for (int k = 0; k < width - 2; k++)
                    {
                        sb.append(horizontalLine);
                    }
                    sb.append(horizontalLine);
                    // sb.append("|--->");
                    // sb.append("|____");
                }
            } else
            {
                if (newLastChildList.get(i + 1))
                {
                    for (int k = 0; k < width; k++)
                    {
                        sb.append(" ");
                    }
                    // sb.append("     ");
                } else
                {
                    sb.append(verticalLine);
                    for (int k = 0; k < width - 1; k++)
                    {
                        sb.append(" ");
                    }
                    // sb.append("|    ");
                }
            }
        }
        
        sb.append(tree.getSelf().toString());
        sb.append("\n");
        
        newLastChildList.add(false);
        currentDepth++;
        
        for (int i = 0; i < tree.getChildCount(); i++)
        {
            if (i + 1 >= tree.getChildCount())
            {
                newLastChildList.set(currentDepth, true);
            } else
            {
                newLastChildList.set(currentDepth, false);
            }
            recurseToPrintString(tree.getChild(i), sb, currentDepth, newLastChildList);
        }
    }
    
    
    protected List<T> recurseToBreadthFirst(Tree<T> tree)
    {
        List<Tree<T>> queue = new ArrayList<Tree<T>>();
        List<T> objs = new ArrayList<T>();
        queue.add(tree);
        
        while (queue.size() > 0)
        {
            Tree<T> curTree = queue.get(0);
            queue.remove(0);
            objs.add(curTree.getSelf());
            
            for (int i = 0; i < curTree.getChildCount(); i++)
            {
                Tree<T> childTree = curTree.getChild(i);
                boolean found = false;
                for (int k = 0; k < objs.size(); k++)
                {
                    if (childTree.getSelf().equals(objs.get(k)))
                        found = true;
                }
                if (!found)
                    queue.add(childTree);
            }
        }
        
        return objs;
    }
    
    
    protected List<T> recurseToDepthFirst(Tree<T> tree, List<T> objs, int currentDepth)
    {
        objs.add(tree.getSelf());
        
        currentDepth++;
        
        for (int i = 0; i < tree.getChildCount(); i++)
        {
            List<T> temp = new ArrayList<T>();
            temp = recurseToDepthFirst(tree.getChild(i), objs, currentDepth);
            objs = temp;
        }
        
        return objs;
    }
    
    
    protected List<Tree<T>> recurseForTreeList(Tree<T> tree, List<Tree<T>> objs, int currentDepth)
    {
        objs.add(tree);
        
        currentDepth++;
        
        for (int i = 0; i < tree.getChildCount(); i++)
        {
            List<Tree<T>> temp = new ArrayList<Tree<T>>();
            temp = recurseForTreeList(tree.getChild(i), objs, currentDepth);
            objs = temp;
        }
        
        return objs;
    }
}
