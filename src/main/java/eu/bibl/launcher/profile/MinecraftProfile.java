package eu.bibl.launcher.profile;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.Proxy;
import java.util.UUID;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import com.mojang.authlib.Agent;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;

import eu.bibl.launcher.profile.providers.ProfileProvider;

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
	
	private static BASE64Encoder enc = new BASE64Encoder();
	private static BASE64Decoder dec = new BASE64Decoder();
	
	public String encrypt(String pass) {
		try {
			String encrypted = enc.encode(pass.getBytes("UTF-8"));
			return xorMessage(encrypted);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public String decrypt(String text) {
		try {
			text = xorMessage(text);
			return new String(dec.decodeBuffer(text), "UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private String xorMessage(String s) {
		try {
			if ((s == null) || (loginUsername == null))
				return null;
			
			char[] keys = loginUsername.toCharArray();
			char[] mesg = s.toCharArray();
			
			int ml = mesg.length;
			int kl = keys.length;
			char[] newmsg = new char[ml];
			
			for (int i = 0; i < ml; i++) {
				newmsg[i] = (char) (mesg[i] ^ keys[i % kl]);
			}// for i
			mesg = null;
			keys = null;
			return new String(newmsg);
		} catch (Exception e) {
			return null;
		}
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