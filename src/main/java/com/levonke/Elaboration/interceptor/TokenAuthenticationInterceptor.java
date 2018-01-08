package com.levonke.Elaboration.interceptor;

import com.levonke.Elaboration.Application;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.security.MessageDigest;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Component
public class TokenAuthenticationInterceptor extends HandlerInterceptorAdapter {
	
	private final String serverSecret = "secret";
	
	private List<Client> clients;
	
	public TokenAuthenticationInterceptor() {
		try {
			DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document document = documentBuilder.parse(Application.class.getClassLoader().getResourceAsStream("auth.xml"));
			NodeList nodeList = document.getElementsByTagName("client");
			Stream<Element> nodeStream = IntStream.range(0, nodeList.getLength()).mapToObj(index -> (Element) nodeList.item(index));
			clients = nodeStream
					.map(node -> new Client()
							.setId(Integer.parseInt(node.getElementsByTagName("id").item(0).getTextContent()))
							.setSecret(node.getElementsByTagName("secret").item(0).getTextContent()))
					.collect(Collectors.toList());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		String authToken = request.getHeader("Auth-Token");
		String authorization = request.getHeader("Authorization");
		if (authToken == null) {
			Client client = clientByAuthorizationCredentials(authorization);
			if (client != null) {
				response.setHeader("Auth-Token", createAuthToken(client));
				return true;
			}
		} else {
			if (checkAuthToken(authToken)) return true;
		}
		response.setStatus(HttpStatus.UNAUTHORIZED.value());
		response.setHeader("Content-Type", "application/json");
		response.getWriter().write("{\"Success\": false}");
		return false;
	}
	
	private Client clientByAuthorizationCredentials(String authorization) {
		AtomicReference<Client> clientByAuthorization = new AtomicReference<>(null);
		if (authorization != null && authorization.startsWith("Basic")) {
			String base64Credentials = authorization.substring("Basic".length()).trim();
			String[] credentials = new String(DatatypeConverter.parseBase64Binary(base64Credentials)).split(":");
			Client checkClient = new Client()
					.setId(tryParseId(credentials[0]))
					.setSecret(credentials[1]);
			clients.forEach((client -> {
				if (client.getId().equals(checkClient.getId()) && client.getSecret().equals(checkClient.getSecret())) {
					clientByAuthorization.set(client);
				}
			}));
		}
		return clientByAuthorization.get();
	}
	
	private Boolean checkAuthToken(String token) {
		try {
			AtomicReference<String> clientSecret = new AtomicReference<>("");
			String authToken = token.split(":")[0];
			String expirationToken = token.split(":")[1];
			Integer clientId = Integer.parseInt(token.split(":")[2]);
			Date expirationDate = new Date(Long.parseLong(token.split(":")[3]));
			
			clients.forEach(client -> {
				if (client.getId().equals(clientId)) { clientSecret.set(client.getSecret()); }
			});
			
			MessageDigest authDigest = MessageDigest.getInstance("SHA-1");
			authDigest.update((clientSecret + serverSecret).getBytes("UTF-8"));
			String authTokenCanonical = DatatypeConverter.printHexBinary(authDigest.digest());
			
			authDigest.reset();
			authDigest.update((expirationDate.getTime() + serverSecret).getBytes("UTF-8"));
			String expirationTokenCanonical = DatatypeConverter.printHexBinary(authDigest.digest());
			
			return (authToken.equals(authTokenCanonical)
					&& expirationToken.equals(expirationTokenCanonical)
					&& expirationDate.after(new Date())
			);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	private String createAuthToken(Client client) {
		Date expirationDate = new Date(System.currentTimeMillis() + (60 * 60 * 1000));
		String authToken = "", expirationToken = "";
		String auth = client.getSecret() + serverSecret;
		String expiration = expirationDate.getTime() + serverSecret;
		try {
			MessageDigest authDigest = MessageDigest.getInstance("SHA-1");
			authDigest.update(auth.getBytes("UTF-8"));
			authToken = DatatypeConverter.printHexBinary(authDigest.digest());
			
			authDigest.reset();
			authDigest.update(expiration.getBytes("UTF-8"));
			expirationToken = DatatypeConverter.printHexBinary(authDigest.digest());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return authToken + ":" + expirationToken + ":" + client.getId() + ":" + expirationDate.getTime();
	}
	
	private int tryParseId(String value) {
		try {
			return Integer.parseInt(value);
		} catch (NumberFormatException nfe) {
			return -1;
		}
	}
}

@Data
@Accessors(chain = true)
class Client {
	private Integer id;
	private String secret;
}