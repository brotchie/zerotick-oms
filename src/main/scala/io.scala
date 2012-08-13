/*   ____  __  __ ___                                           *\
**  / _//\|  \/  / __|   zerotick-oms - The Minimalist Order    **
** | (//) | |\/| \__ \                  Management System       **
**  \//__/|_|  |_|___/   Copyright (c) 2012, James Brotchie     **
\*                       http://zerotick.org/                   */

package zerotick.ib.io  

import java.io.{ InputStream
               , DataInputStream }

/** A DateInputStream where the underlying input stream
  * is replaceable.
  *
  * A ReplaceableDataInputStream is passed to the IB API
  * EReader object. This lets us parse arbitrary Array[Byte]
  * objects using the native Java IB API.
  */
class ReplaceableDataInputStream() extends DataInputStream(null) {
  var instream: InputStream = null
  def replace(stream: InputStream) { in = stream }
}
