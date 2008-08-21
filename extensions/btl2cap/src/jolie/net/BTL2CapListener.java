/***************************************************************************
 *   Copyright (C) by Fabrizio Montesi                                     *
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU Library General Public License as       *
 *   published by the Free Software Foundation; either version 2 of the    *
 *   License, or (at your option) any later version.                       *
 *                                                                         *
 *   This program is distributed in the hope that it will be useful,       *
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of        *
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the         *
 *   GNU General Public License for more details.                          *
 *                                                                         *
 *   You should have received a copy of the GNU Library General Public     *
 *   License along with this program; if not, write to the                 *
 *   Free Software Foundation, Inc.,                                       *
 *   59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.             *
 *                                                                         *
 *   For details about the authors of this software, see the AUTHORS file. *
 ***************************************************************************/

package jolie.net;

import java.io.IOException;
import java.net.URI;
import java.nio.channels.ClosedByInterruptException;
import java.util.Collection;
import java.util.Map;

import javax.bluetooth.L2CAPConnection;
import javax.bluetooth.L2CAPConnectionNotifier;
import javax.microedition.io.Connector;
import jolie.Interpreter;

public class BTL2CapListener extends CommListener
{
	final private L2CAPConnectionNotifier connectionNotifier;
	public BTL2CapListener(
				Interpreter interpreter,
				URI location,
				CommProtocol protocol,
				Collection< String > operationNames,
				Map< String, OutputPort > redirectionMap
			)
		throws IOException
	{
		super( interpreter, protocol, operationNames, redirectionMap );
		connectionNotifier =
				(L2CAPConnectionNotifier)Connector.open( location.toString() );
	}
	
	@Override
	public void run()
	{
		try {
			L2CAPConnection clientConnection;
			CommChannel channel;
			while ( (clientConnection = connectionNotifier.acceptAndOpen()) != null ) {
				channel = new BTL2CapCommChannel(
							clientConnection,
							createProtocol() );
				channel.setParentListener( this );
				interpreter().commCore().scheduleReceive( channel, this );
				channel = null; // Dispose for garbage collection
			}
			connectionNotifier.close();
		} catch( ClosedByInterruptException ce ) {
			try {
				connectionNotifier.close();
			} catch( IOException ioe ) {
				ioe.printStackTrace();
			}
		} catch( IOException e ) {
			e.printStackTrace();
		}
	}
}
