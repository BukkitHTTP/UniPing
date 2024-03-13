package prot.mc;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class PacketUtils {
    public static void sendPacket(DataOutputStream dos, byte[] packetData) throws IOException {
        PacketUtils.writeVarInt(dos, packetData.length);
        dos.write(packetData);
    }

    private static void writeString(DataOutputStream out, String value) throws IOException {
        byte[] data = value.getBytes(StandardCharsets.UTF_8);
        writeVarInt(out, data.length);
        out.write(data, 0, data.length);
    }

    public static void writeVarInt(DataOutputStream out, int value) throws IOException {
        while ((value & -128) != 0) {
            out.writeByte(value & 127 | 128);
            value >>>= 7;
        }
        out.writeByte(value);
    }

    public static int readVarInt(InputStream in) throws IOException {
        int numRead = 0;
        int result = 0;
        byte read;
        do {
            read = (byte) in.read();
            int value = (read & 127);
            result |= (value << (7 * numRead));
            numRead++;
            if (numRead > 5) {
                throw new RuntimeException("VarInt too big");
            }
        } while ((read & 128) != 0);
        return result;
    }

    public static byte[] createHandshakePacket(String ip, int port, int protocol, int nextState) throws IOException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(bytes);
        writeVarInt(out, 0);
        writeVarInt(out, protocol);
        writeString(out, ip);
        out.writeShort(port);
        writeVarInt(out, nextState);
        byte[] data = bytes.toByteArray();
        bytes.close();
        return data;
    }

    public static byte[] createPingPacket(boolean wantResp) throws IOException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(bytes);
        writeVarInt(out, wantResp ? 0 : 1);
        if (!wantResp) {
            out.writeLong(System.currentTimeMillis());
        }
        byte[] data = bytes.toByteArray();
        bytes.close();
        return data;
    }
}
