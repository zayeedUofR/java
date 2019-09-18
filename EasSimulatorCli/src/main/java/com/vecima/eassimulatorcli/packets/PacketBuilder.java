/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vecima.eassimulatorcli.packets;

import com.vecima.eassimulatorcli.util.StringProcessor;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import org.pcap4j.packet.EthernetPacket;
import org.pcap4j.packet.IpV4Packet;
import org.pcap4j.packet.IpV4Rfc791Tos;
//import org.pcap4j.packet.IpV6Packet;
//import org.pcap4j.packet.IpV6SimpleFlowLabel;
//import org.pcap4j.packet.IpV6SimpleTrafficClass;
import org.pcap4j.packet.Packet;
import org.pcap4j.packet.UdpPacket;
import org.pcap4j.packet.UnknownPacket;
import org.pcap4j.packet.namednumber.EtherType;
import org.pcap4j.packet.namednumber.IpNumber;
import org.pcap4j.packet.namednumber.IpVersion;
import org.pcap4j.packet.namednumber.UdpPort;
import org.pcap4j.util.MacAddress;

/**
 *
 * @author zayeed
 */
public class PacketBuilder {

    public static String srcIP = "192.168.216.56";
    public static String dstIP = "239.5.150.150";
    public static int inputSrcPort = 32769;
    public static int inputDstPort = 5050;
    public static int inputTTL = 10;
    public static String srcMacAddress = "00:13:3b:10:31:5b";
    public static String dstMacAddress = "";

    private final UdpPort srcPort;
    private final UdpPort dstPort;
    private final short length;
    private final short checksum;
    private final Inet4Address srcAddr;
    private final Inet4Address dstAddr;
    private UdpPacket packet;
    private final IpV4Packet.IpV4Tos tos;

    public PacketBuilder() throws Exception{
        this.dstMacAddress = StringProcessor.multicast_ip_to_mac(dstIP);
        this.srcPort = UdpPort.getInstance((short) inputSrcPort); //UdpPort.SNMP;
        this.dstPort = UdpPort.getInstance((short) inputDstPort);
        this.length = (short) 12;
        this.checksum = (short) 0xABCD;
        this.tos = IpV4Rfc791Tos.newInstance((byte) 0);
        try {
            this.srcAddr
                    = (Inet4Address) InetAddress.getByName(srcIP);
            this.dstAddr
                    = (Inet4Address) InetAddress.getByName(dstIP);
        } catch (UnknownHostException e) {
            throw new AssertionError();
        }
    }

    public Packet getwholePacket(byte[] mpegHeader) {
        UnknownPacket.Builder unknownb = new UnknownPacket.Builder();
        unknownb.rawData(mpegHeader);

        UdpPacket.Builder b = new UdpPacket.Builder();
        b.dstPort(dstPort)
                .srcPort(srcPort)
                .length(length)
                .checksum(checksum)
                .correctChecksumAtBuild(false)
                .correctLengthAtBuild(false) 
                .payloadBuilder(unknownb);

        this.packet = b.build();
        IpV4Packet.Builder IpV4b = new IpV4Packet.Builder();
        IpV4b.version(IpVersion.IPV4)
                .ihl((byte) 5)
                .tos(tos)
                .totalLength((short) 216)
                .identification((short) 0)
                .fragmentOffset((short) 0)
                .dontFragmentFlag(true)
                .srcAddr(srcAddr)
                .dstAddr(dstAddr)
                .ttl((byte) inputTTL)
                .protocol(IpNumber.UDP)
                .correctChecksumAtBuild(true)
                .payloadBuilder(
                        packet.getBuilder()
                                .correctChecksumAtBuild(true)
                                .correctLengthAtBuild(true)
                )
                .correctLengthAtBuild(true);

        EthernetPacket.Builder eb = new EthernetPacket.Builder();
        eb.dstAddr(MacAddress.getByName(dstMacAddress))
                .srcAddr(MacAddress.getByName(srcMacAddress))
                .type(EtherType.IPV4)
                .payloadBuilder(IpV4b)
                //.pad(new byte[]{}) // builder pad must not be null if paddingAtBuild is false
                .paddingAtBuild(true);

        eb.get(UdpPacket.Builder.class)
                .dstAddr(dstAddr)
                .srcAddr(srcAddr);
        return eb.build();

    }

}
