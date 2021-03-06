/*
 * Copyright 2010 LinkedIn
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package kafka.zk

import org.apache.zookeeper.server.ZooKeeperServer
import org.apache.zookeeper.server.NIOServerCnxn
import kafka.utils.TestUtils
import org.I0Itec.zkclient.ZkClient
import java.net.InetSocketAddress
import kafka.utils.{Utils, StringSerializer}

class EmbeddedZookeeper(val connectString: String) {
  val snapshotDir = TestUtils.tempDir()
  val logDir = TestUtils.tempDir()
  val zookeeper = new ZooKeeperServer(snapshotDir, logDir, 3000)
  val port = connectString.split(":")(1).toInt
  val factory = new NIOServerCnxn.Factory(new InetSocketAddress("127.0.0.1", port))
  factory.startup(zookeeper)
  val client = new ZkClient(connectString)
  client.setZkSerializer(StringSerializer)

  def shutdown() {
    factory.shutdown()
    Utils.rm(logDir)
    Utils.rm(snapshotDir)
  }
  
}
