package eu.bibl.launcher.profile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.Proxy;
import java.util.UUID;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import com.mojang.authlib.Agent;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;

public class MinecraftProfile {
	
	private String username;
	private String password;
	
	public MinecraftProfile(String username, String password) {
		this.username = username;
		this.password = encrypt(password);
	}
	
	public YggdrasilUserAuthentication login() throws AuthenticationException {
		YggdrasilUserAuthentication auth = loginWithPass(getPassword());
		return auth;
	}
	
	private YggdrasilUserAuthentication loginWithPass(String pass) throws AuthenticationException {
		final YggdrasilAuthenticationService service = new YggdrasilAuthenticationService(Proxy.NO_PROXY, UUID.randomUUID().toString());
		final YggdrasilUserAuthentication auth = new YggdrasilUserAuthentication(service, Agent.MINECRAFT);
		
		auth.setUsername(username);
		auth.setPassword(pass);
		auth.logIn();
		
		return auth;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(final String username) {
		this.username = username;
	}
	
	public String getPassword() {
		return decrypt(password);
	}
	
	public String getRawPassword() {
		return password;
	}
	
	public void setPassword(String newPassword) {
		if (newPassword.contains("*"))
			return;
		String oldPassword = decrypt(password);
		if (!oldPassword.equals(newPassword)) {
			password = encrypt(newPassword);
		}
	}
	
	private String encrypt(String pass) {
		byte[] compressed = new byte[] {};
		final Deflater compressor = new Deflater();
		compressor.setLevel(Deflater.BEST_COMPRESSION);
		compressor.setInput(pass.getBytes());
		compressor.finish();
		try (ByteArrayOutputStream bos = new ByteArrayOutputStream(pass.length())) {
			final byte[] buf = new byte[1024];
			while (!compressor.finished()) {
				final int count = compressor.deflate(buf);
				bos.write(buf, 0, count);
			}
			compressed = bos.toByteArray();
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
		return new String(compressed);
	}
	
	private String decrypt(String pass) {
		final Inflater decompressor = new Inflater();
		decompressor.setInput(pass.getBytes());
		byte[] decompressed = new byte[] {};
		final ByteArrayOutputStream bos = new ByteArrayOutputStream(pass.length());
		try {
			final byte[] buf = new byte[1024];
			while (!decompressor.finished()) {
				try {
					final int count = decompressor.inflate(buf);
					bos.write(buf, 0, count);
				} catch (final DataFormatException e) {
					throw new RuntimeException(e);
				}
			}
			decompressed = bos.toByteArray();
			bos.close();
		} catch (final IOException e) {
		}
		return new String(decompressed);
	}
	
	@Override
	public String toString() {
		return username;
	}
}