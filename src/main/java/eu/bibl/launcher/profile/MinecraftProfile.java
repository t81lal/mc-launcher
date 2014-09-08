package eu.bibl.launcher.profile;

import com.mojang.authlib.Agent;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import eu.bibl.launcher.profile.providers.ProfileProvider;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.Proxy;
import java.util.UUID;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class MinecraftProfile {
	
	private static Field accessTokenField;
	
	static {
		Class<?> yggdrasilUserAuthenticationClass = YggdrasilUserAuthentication.class;
		for (Field field : yggdrasilUserAuthenticationClass.getDeclaredFields()) {
			if (field.getName().equals("accessToken")) {
				accessTokenField = field;
				accessTokenField.setAccessible(true);
				break;
			}
		}
	}
	
	private String clientToken;
	private String authToken;
	private String gameUsername;
	private String loginUsername;
	private String password;
	
	public MinecraftProfile(String loginUsername, String password) {
		this.loginUsername = loginUsername;
		this.password = encrypt(password);
		clientToken = UUID.randomUUID().toString();
	}
	
	public YggdrasilUserAuthentication login(ProfileProvider provider) throws AuthenticationException {
		if (authToken != null) {
			try {
				YggdrasilUserAuthentication auth = loginWithAuthToken();
				provider.authenticated(this, auth);
				return auth;
			} catch (AuthenticationException e) {
				setRandomToken();
			}
		}
		
		try {
			YggdrasilUserAuthentication auth = loginWithPass();
			provider.authenticated(this, auth);
			return auth;
		} catch (AuthenticationException e) {
			throw e;
		}
		
	}
	
	private YggdrasilUserAuthentication loginWithAuthToken() throws AuthenticationException {
		YggdrasilAuthenticationService service = new YggdrasilAuthenticationService(Proxy.NO_PROXY, clientToken);
		YggdrasilUserAuthentication auth = new YggdrasilUserAuthentication(service, Agent.MINECRAFT);
		
		auth.setUsername(loginUsername);
		try {
			accessTokenField.set(auth, authToken);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			throw new AuthenticationException("Unable to set accessToken field.");
		}
		auth.logIn();
		
		gameUsername = auth.getSelectedProfile().getName();
		
		return auth;
	}
	
	private YggdrasilUserAuthentication loginWithPass() throws AuthenticationException {
		YggdrasilAuthenticationService service = new YggdrasilAuthenticationService(Proxy.NO_PROXY, setRandomToken());
		YggdrasilUserAuthentication auth = new YggdrasilUserAuthentication(service, Agent.MINECRAFT);
		
		auth.setUsername(loginUsername);
		auth.setPassword(getPassword());
		auth.logIn();
		
		authToken = auth.getAuthenticatedToken();
		gameUsername = auth.getSelectedProfile().getName();
		
		return auth;
	}
	
	public String getClientToken() {
		return clientToken;
	}
	
	public void setClientToken(String clientToken) {
		this.clientToken = clientToken;
	}
	
	public String setRandomToken() {
		clientToken = UUID.randomUUID().toString();
		return clientToken;
	}
	
	public String getAuthenticatedToken() {
		return authToken;
	}
	
	public String getLoginUsername() {
		return loginUsername;
	}
	
	public void setLoginUsername(String loginUsername) {
		this.loginUsername = loginUsername;
	}
	
	public String getGameUsername() {
		return gameUsername;
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
		byte[] compressed;
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
		return gameUsername;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MinecraftProfile other = (MinecraftProfile) obj;
		return other.gameUsername.equals(gameUsername) && other.loginUsername.equals(loginUsername);
	}
}