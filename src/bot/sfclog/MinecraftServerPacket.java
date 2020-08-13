package bot.sfclog;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;


public final class MinecraftServerPacket {
    public static byte[] readByteArray(DataInputStream in) {
        int length = MinecraftServerPacket.readVarInt(in);
        byte[] data = new byte[length];
        try {
            in.readFully(data);
        }
        catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        return data;
    }

    public static int readVarInt(DataInputStream in) {
        int numRead = 0;
        int result = 0;
        try {
            byte read;
            do {
                read = in.readByte();
                int value = read & 127;
                result |= value << 7 * numRead;
                if (++numRead <= 5) continue;
                throw new RuntimeException("VarInt is too big");
            } while ((read & 128) != 0);
        }
        catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        return result;
    }

    public static void writeVarInt(DataOutputStream out, int paramInt) {
        try {
            do {
                if ((paramInt & -128) == 0) {
                    out.writeByte(paramInt);
                    return;
                }
                out.writeByte(paramInt & 127 | 128);
                paramInt >>>= 7;
            } while (true);
        }
        catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static void writeVarIntException(DataOutputStream out, int paramInt) throws IOException {
        do {
            if ((paramInt & -128) == 0) {
                out.writeByte(paramInt);
                return;
            }
            out.writeByte(paramInt & 127 | 128);
            paramInt >>>= 7;
        } while (true);
    }

    public static void writeVarInt(ByteArrayOutputStream out, int paramInt) {
        do {
            if ((paramInt & -128) == 0) {
                out.write(paramInt);
                return;
            }
            out.write(paramInt & 127 | 128);
            paramInt >>>= 7;
        } while (true);
    }

    public static void writeByteArray(DataOutputStream out, byte[] data) {
        try {
            MinecraftServerPacket.writeVarInt(out, data.length);
            out.write(data, 0, data.length);
        }
        catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static void writePacket(DataOutputStream out, byte[] packet) {
        try {
            MinecraftServerPacket.writeVarInt(out, packet.length);
            out.write(packet);
        }
        catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static void writePacketException(DataOutputStream out, byte[] packet) throws IOException {
        MinecraftServerPacket.writeVarIntException(out, packet.length);
        out.write(packet);
    }

    public static byte[] serialize(Object obj) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ObjectOutputStream os = new ObjectOutputStream(out);
            os.writeObject(obj);
            return out.toByteArray();
        }
        catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static Object deserialize(byte[] data) {
        try {
            ByteArrayInputStream in = new ByteArrayInputStream(data);
            ObjectInputStream is = new ObjectInputStream(in);
            return is.readObject();
        }
        catch (IOException | ClassNotFoundException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static byte[] createEncryptionResponsePacket(byte[] encryptedKey, byte[] encryptedVerifyToken) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(bytes);
        MinecraftServerPacket.writeVarInt(out, 1);
        MinecraftServerPacket.writeByteArray(out, encryptedKey);
        MinecraftServerPacket.writeByteArray(out, encryptedVerifyToken);
        byte[] data = bytes.toByteArray();
        try {
            bytes.close();
        }
        catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        return data;
    }
    
    public static byte[] createHandshakeMessage18(String host, int port, int state) {
        try {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            DataOutputStream handshake = new DataOutputStream(buffer);
            handshake.writeByte(0);
            MinecraftServerPacket.writeVarInt(handshake, 47);
            MinecraftServerPacket.writeString(handshake, host, StandardCharsets.UTF_8);
            handshake.writeShort(port);
            MinecraftServerPacket.writeVarInt(handshake, state);
            return buffer.toByteArray();
        }
        catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    public static byte[] createHandshakeMessageMods(String host, int port, int state) {
        try {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            DataOutputStream handshake = new DataOutputStream(buffer);
            handshake.writeByte(0);
            MinecraftServerPacket.writeVarInt(handshake, 47);
            MinecraftServerPacket.writeString(handshake, host, StandardCharsets.UTF_8);
            handshake.writeUTF(host);
            handshake.writeShort(port);
            MinecraftServerPacket.writeVarInt(handshake, state);
            return buffer.toByteArray();
        }
        catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    public static byte[] createLogin(String username) {
        try {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            DataOutputStream login = new DataOutputStream(buffer);
            login.writeByte(0);
            MinecraftServerPacket.writeString(login, username, StandardCharsets.UTF_8);
            return buffer.toByteArray();
        }
        catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static void writeString(DataOutputStream out, String string, Charset charset) {
        try {
            byte[] bytes = string.getBytes(charset);
            MinecraftServerPacket.writeVarInt(out, bytes.length);
            out.write(bytes);
        }
        catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static void writeString(ByteArrayOutputStream out, String string, Charset charset) {
        try {
            byte[] bytes = string.getBytes(charset);
            MinecraftServerPacket.writeVarInt(out, bytes.length);
            out.write(bytes);
        }
        catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static void sendPacket(byte[] packet, DataOutputStream out) {
        MinecraftServerPacket.writePacket(out, packet);
    }

    public static void sendPacketException(byte[] packet, DataOutputStream out) throws IOException {
        MinecraftServerPacket.writePacketException(out, packet);
    }
}

