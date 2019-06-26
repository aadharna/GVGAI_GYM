import logging
import os
import socket
import sys
import traceback
import time
from struct import pack_into, unpack_from

class IOSocket:
    """
     * Socket for communication
    """

    def __init__(self, tmpDir):
        self.HEADER_SIZE = 13
        self.BUFFER_SIZE = 8192*10
        self.hostname, self.port = self.getOpenAddress()
        #self.port = 8083
        self.connected = False
        self.socket = None
        self._last_message_id = 0
        self.logfilename = os.path.normpath(os.path.join(tmpDir, "logs", "clientLog.txt"))

        os.makedirs(os.path.join(tmpDir, 'logs'), exist_ok=True)
        self.logfile = open(self.logfilename, "a")

    def initBuffers(self):
        print ("Connecting to host " + str(self.hostname) + " at port " + str(self.port) + " ...")
        while not self.connected:
            try:
                self.socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
                self.socket.setsockopt(socket.IPPROTO_TCP, socket.TCP_NODELAY, 1)
                self.socket.setsockopt(socket.IPPROTO_TCP, socket.TCP_QUICKACK, 1)
                self.socket.connect((self.hostname, self.port))
                self.connected = True
                print ("Client connected to server [OK]")
            except Exception as e:
                time.sleep(1)

    def writeToFile(self, line):
        sys.stdout.write(line + os.linesep)
        self.logfile.write(line + os.linesep)
        sys.stdout.flush()
        self.logfile.flush()

    def writeToServer(self, agent_phase, data=None, log=False):

        payload_size = len(data) if data is not None else 0
        buffer_size = 13 + payload_size

        agent_phase = bytes([agent_phase.value])

        buffer = bytearray(buffer_size)

        # Long in java is 8 bytes
        pack_into('>q', buffer, 0, self._last_message_id)
        pack_into('c', buffer, 8, agent_phase)

        if data is not None:
            pack_into('>i', buffer, 9, payload_size)
            pack_into('%ds' % payload_size, buffer, 13, data)
        else:
            pack_into('>i', buffer, 9, 0)

        try:
            self.socket.send(buffer)
            if log:
                self.writeToFile(buffer)
        except Exception as e:
            logging.exception(e)
            print ("Write " + self.logfilename + " to server [FAILED]")
            traceback.print_exc()
            sys.exit()

    def shutDown(self):
        self.socket.shutdown(0)

    def getOpenAddress(self):
        s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        s.bind(("localhost",0))
        address = s.getsockname()
        s.close()
        return address

    def _read_until(self, length):
        buffer = bytearray()
        while len(buffer) < length:
            recv_size = min(length-len(buffer), self.BUFFER_SIZE)
            buffer += bytearray(self.socket.recv(recv_size))
        return buffer

    
    def readFromServer(self):
        # Firstly read the header bytes
        header_buffer = self._read_until(self.HEADER_SIZE)
        self._last_message_id, game_phase, message_size = unpack_from('>qci', header_buffer, 0)
        data = None
        if message_size > 0:
            data = self._read_until(message_size)
        return game_phase, data

