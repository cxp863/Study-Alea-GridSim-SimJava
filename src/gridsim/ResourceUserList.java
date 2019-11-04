/*
 * Title:        GridSim Toolkit
 * Description:  GridSim (Grid Simulation) Toolkit for Modeling and Simulation
 *               of Parallel and Distributed Systems such as Clusters and Grids
 * License:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 */

package gridsim;

import java.util.LinkedList;

/**
 * GridSim ResourceUserList maintains a linked-list of Grid Resource users
 * ResourceUserList 维护一个 Grid Resource users 链表，它继承了LinkedList类，
 * 虽然它说自己维护了Grid Resource 链表，实际上只维护了整数类型的用户ID
 *
 * @author       Manzur Murshed and Rajkumar Buyya
 * @since        GridSim Toolkit 1.0
 * @invariant $none
 */
public class ResourceUserList extends LinkedList<Integer> {

	/**
     * Adds one Grid Resource user into the list.
     * @param userID Grid Resource user ID
     * @return <b>true</b> if it is a new user, otherwise return <b>false</b>
     * @pre userID >= 0
     * @post $result == true || $result == false
     */
    public boolean add(int userID) {
        Integer user = userID;

        // 其实这里装箱时没有任何必要的，contains只接受Object，这时候基本类型int会被包装成Integer再传入的
        // check whether the user has already stored into the list
        if (super.contains(user)) {
            return false;
        } else {
            super.add(user);
            return true;
        }
    }

    /**
     * Removes a particular user from the list
     * @param userID Grid Resource user ID
     * @return <b>true</b> if the list contained the specified element,
     *         otherwise return <b>false</b>
     * @deprecated As of GridSim 2.1, replaced by {@link #removeUser(int)}
     * @pre userID >= 0
     * @post $result == true || $result == false
     */
    public boolean myRemove(int userID) {
        return this.removeUser(userID);
    }

    /**
     * Removes a particular user from the list
     * @param userID Grid Resource user ID
     * @return <b>true</b> if the list contained the specified element,
     *         otherwise return <b>false</b>
     * @pre userID >= 0
     * @post $result == true || $result == false
     */
    public boolean removeUser(int userID) {
        return super.remove( new Integer(userID) );
    }

} 

