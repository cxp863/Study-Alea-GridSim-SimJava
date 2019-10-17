/*
 * ** Network and Service Differentiation Extensions to GridSim 3.0 **
 *
 * Gokul Poduval & Chen-Khong Tham
 * Computer Communication Networks (CCN) Lab
 * Dept of Electrical & Computer Engineering
 * National University of Singapore
 * August 2004
 *
 * License: GPL - http://www.gnu.org/copyleft/gpl.html
 * Copyright (c) 2004, The University of Melbourne, Australia and National
 * University of Singapore
 * Link.java - Abstract class as a template for a Network Link
 *
 */

package gridsim.net;

import eduni.simjava.*;
import gridsim.*;
import java.util.*;


/**
 * This class provides a template for a Link which can connect two entities like
 * routers, GridResources or any other new component that needs to use the
 * network facilities in Gridsim.
 * <p>
 * In the network model used here, the links only
 * introduce propogation delays. Routers are resposible for queueing and
 * transmission delays. Also, links do not check whether the packets being sent
 * through are of size smaller than the MTU. It is the responsibility of the
 * upstream sender to check the size of the packet. The baud rate and the MTU
 * attributes are stored in the links to facilitate routers and other entities
 * that send data through a link.
 *
 * 此类为Link提供了一个模板，该模板可以连接两个实体，例如路由器，
 * GridResources或需要使用Gridsim中的网络设施的任何其他新组件。
 * 在此处使用的网络模型中，链接仅引入传播延迟。 路由器可用于排队和传输延迟。
 * 此外，链接不会检查通过其发送的数据包的大小是否小于MTU。
 * 上游发送者有责任检查数据包的大小。 波特率和MTU属性存储在链接中，以方便路由器和其他通过链接发送数据的实体。
 *
 * @invariant $none
 * @since GridSim Toolkit 3.1
 * @author Gokul Poduval & Chen-Khong Tham, National University of Singapore
 */
public abstract class Link extends Sim_entity
{
    /** Propagation delay of this link in millisecond */
    protected double delay_;

    /** Baud rate of this link in bits/s */
    protected double baudRate_;

    /** Maximum Transmission Unit (MTU) of this link in bytes */
    protected int MTU_;     // packet size

    /** Default baud rate of a link in bits/sec */
    public static final int DEFAULT_BAUD_RATE = 9600;

    /** Default propagation delay of a link in millisecond */
    public static final int DEFAULT_PROP_DELAY = 5;

    /** Default Maximum Transmission Unit (MTU) of a link in bytes */
    public static final int DEFAULT_MTU = 1500;

    /** A constant represents 1 second in milliseconds */
    protected final int MILLI_SEC = 1000; 
    
    /**
     * Constructs a Link which simulates a physical link between two entities.
     *
     * @param name      Name of this Link
     * @param baudRate  baud rate of this link (bits/s)
     * @param propDelay Propogation delay of the Link in milli seconds
     * @param MTU       Maximum Transmission Unit of the Link in bytes.
     *                  Packets which are larger than the MTU should be split
     *                  up into MTU size units. <br>
     *                  For e.g. a 1024 byte packet trying to cross a 576 byte
     *                  MTU link should get split into 2 packets of 576 bytes
     *                  and 448 bytes.
     * @throws NullPointerException This happens when name is null or empty
     * @throws ParameterException   This happens for the following conditions:
     *      <ul>
     *          <li> name is null
     *          <li> baudRate <= 0
     *          <li> propDelay <= 0
     *          <li> MTU <= 0
     *      </ul>
     *
     * @pre name != null
     * @pre baudRate > 0
     * @pre propDelay > 0
     * @pre MTU > 0
     * @post $none
     */
    public Link(String name, double baudRate, double propDelay, int MTU)
                throws ParameterException, NullPointerException
    {
        super(name);
        String msg = name + "(): Error - ";
        if (baudRate <= 0) {
            throw new ParameterException(msg + "baud rate must be > 0.");
        }
        else if (propDelay <= 0) {
            throw new ParameterException(msg+"propagation delay must be > 0.");
        }
        else if (MTU <= 0) {
            throw new ParameterException(msg + "MTU must be > 0.");
        }

        this.baudRate_ = baudRate;
        this.delay_ = propDelay;
        this.MTU_ = MTU;
    }

    /**
     * Connects two entities using this link. Any data sent through one end of
     * this link will be sent out through the other.
     *
     * @param end1 Entity attached to one end of the Link
     * @param end2 Entity attached to the other end of the Link
     * @pre end1 != null
     * @pre end2 != null
     * @post $none
     */
    public abstract void attach(Sim_entity end1, Sim_entity end2);

    /**
     * Connects two entities using this link. Any data sent through one end of
     * this link will be sent out through the other.
     *
     * @param end1 Entity attached to one end of the Link
     * @param end2 Entity attached to the other end of the Link
     * @pre end1 != null
     * @pre end2 != null
     * @post $none
     */
    public abstract void attach(String end1, String end2);

    /**
     * Returns the baud rate of the link in bits/s.
     *
     * @return the baud rate (bits/s)
     * @pre $none
     * @post $none
     */
    public double getBaudRate() {
        return this.baudRate_;
    }

    /**
     * Returns the tramssion delay that this link introduces.
     *
     * @return tranmission delay in milliseconds
     * @pre $none
     * @post $none
     */
    public double getDelay() {
        return this.delay_;
    }


    /**
     * Returns the Maximum Transmission Unit of this Link
     *
     * @return MTU in bytes
     * @pre $none
     * @post $none
     */
    public int getMTU() {
        return this.MTU_;
    }

} // end class

