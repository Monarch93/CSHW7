package ru.gb.simplenas.server.services.impl;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.gb.simplenas.common.services.impl.NasMsgInboundHandler;
import ru.gb.simplenas.server.services.Server;

import java.util.*;

import static ru.gb.simplenas.common.CommonData.*;
import static ru.gb.simplenas.common.Factory.lnprint;

public class NasServer implements Server
{
    private static NasServer instance;
    private final static Map<String, NasServerManipulator> CHANNELS = new HashMap<>();
    private Thread consoleReader;
    private boolean serverGettingOff;
    private Channel channelOfChannelFuture;
    public static final String CMD_EXIT = "exit";
    private static final Logger LOGGER = LogManager.getLogger(NasServer.class.getName());


    private NasServer() {}

    public static Server getInstance()
    {
        if (instance == null)
        {
            instance = new NasServer();
            instance.run();
        }
        return instance;
    }

    private void run()
    {
        LOGGER.trace("run(): start");

        EventLoopGroup groupParent = new NioEventLoopGroup(1);
        EventLoopGroup groupChild = new NioEventLoopGroup();

        try
        {
            ServerBootstrap sbts = new ServerBootstrap();
            sbts.group (groupParent, groupChild)

                .channel (NioServerSocketChannel.class)
                .childHandler (new ChannelInitializer<SocketChannel>()	//< когда к нам кто-то подключиться, …
                {   @Override
                    protected void initChannel (SocketChannel sC) throws Exception //< … мы его инициализируем в этом методе
                    {
                        LOGGER.trace("\t{}.initChannel (SocketChannel "+ sC.toString()+") start");
                        sC.pipeline().addLast (

                                    new ObjectEncoder(),

                                    new ObjectDecoder (Integer.MAX_VALUE, ClassResolvers.weakCachingConcurrentResolver(null)), //< то же самое, но конкурентно и с небольшим кэшированием
                                    new NasMsgInboundHandler (new NasServerManipulator (sC))
                                    );
                        LOGGER.trace("initChannel() end");

                    }
                });
            ChannelFuture cfuture = sbts.bind(PORT).sync();
            LOGGER.debug("run(): ChannelFuture "+ cfuture.toString());

            channelOfChannelFuture = cfuture.channel();
            if (consoleReader == null)
            {
                consoleReader = new Thread (this::runConsoleReader);
                consoleReader.start();
            }
            cfuture.channel().closeFuture().sync();
            LOGGER.trace("run(): ChannelFuture.channel closed");

        }
        catch (Exception e)   {   LOGGER.error("run(): ", e);   }
        finally
        {
            groupParent.shutdownGracefully();
            groupChild.shutdownGracefully();
            LOGGER.trace("run(): end");
            instance = null;
        }
    }

    private void runConsoleReader()
    {
        LOGGER.trace("runConsoleReader(): start");
        Scanner scanner = new Scanner(System.in);
        try
        {   String msg;
            while (!serverGettingOff && scanner != null)
            {
                msg = scanner.nextLine().trim();

                if (!msg.isEmpty())
                if (msg.equalsIgnoreCase (CMD_EXIT))
                {
                    onCmdServerExit();
                    scanner.close();    scanner = null;
                }
                else LOGGER.warn("runConsoleReader(): unsupported command detected: "+ msg);
            }
        }
        finally
        {   consoleReader = null;
            if (scanner != null)
            {
                scanner.close();
                scanner = null;
            }
            LOGGER.trace("runConsoleReader(): end");
        }
    }


    @Override public boolean clientsListAdd (NasServerManipulator manipulator, String userName)
    {
        boolean ok = false;
        if (!CHANNELS.containsKey (userName))
        {
            CHANNELS.put (userName, manipulator);
            ok = true;
            LOGGER.trace("clientsListAdd(): клиент <"+userName+"> добавлен.");
        }
        return ok;
    }

    @Override public void clientsListRemove (NasServerManipulator manipulator, String userName)
    {
        if (DEBUG) lnprint("NasServer.clientsListRemove(): call.");
        if (userName != null && CHANNELS.get(userName) == manipulator)
        {
            CHANNELS.remove (userName);
            if (DEBUG) lnprint("NasServer.clientsListRemove(): клиент <"+userName+"> удалён.");
        }
    }

    private void closeAllClientConnections()
    {
        Set<Map.Entry<String, NasServerManipulator>> entries = CHANNELS.entrySet();

        for (Map.Entry<String, NasServerManipulator> e : entries)
        {
            NasServerManipulator manipulator = e.getValue();
            manipulator.startExitRequest(null);
        }
    }

    private void onCmdServerExit()
    {
        LOGGER.trace("onCmdServerExit(): start");
        serverGettingOff = true;

        closeAllClientConnections();
        CHANNELS.clear();
        Channel c = channelOfChannelFuture;
        if (channelOfChannelFuture != null && channelOfChannelFuture.isOpen())
        {
            channelOfChannelFuture.disconnect();    //TODO : кажется, так мы соединение не закроем.
        }
        channelOfChannelFuture = null;

        LOGGER.trace("onCmdServerExit(): end");
    }

}