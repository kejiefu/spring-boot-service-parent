package com.mountain.tool.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.security.cert.CertificateException;
import java.util.List;
import java.util.Map;


public class HttpClientUtils {

	public static final String CHARSET_ENCODING = "UTF-8";

	public static final String CONTENT_TYPE_WWW_FORM = "application/x-www-form-urlencoded";

	public static final String CONTENT_TYPE_FORM_DATA = "multipart/form-data";

	public static final String CONTENT_TYPE_JSON = "application/json";

	public static final String CONTENT_TYPE_XML = "text/xml";

	/**
	 * 异常自动恢复处理, 使用HttpRequestRetryHandler接口实现请求的异常恢复
	 */
	private static HttpRequestRetryHandler requestRetryHandler = new HttpRequestRetryHandler() {
		// 自定义的恢复策略
		public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
			// 设置恢复策略，在发生异常时候将自动重试3次
			if (executionCount >= 3) {
				// 如果连接次数超过了最大值则停止重试
				return false;
			}
			if (exception instanceof NoHttpResponseException) {
				// 如果服务器连接失败重试
				return true;
			}
			if (exception instanceof SSLHandshakeException) {
				// 不要重试ssl连接异常
				return false;
			}
			HttpRequest request = (HttpRequest) context.getAttribute(ExecutionContext.HTTP_REQUEST);
			boolean idempotent = (request instanceof HttpEntityEnclosingRequest);
			if (!idempotent) {
				// 重试，如果请求是考虑幂等
				return true;
			}
			return false;
		}
	};

	/**
	 * 使用ResponseHandler接口处理响应，HttpClient使用ResponseHandler会自动管理连接的释放，解决了对连接的释放管理
	 */
	public static ResponseHandler<String> responseHandler = new ResponseHandler<String>() {
		// 自定义响应处理
		public String handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				@SuppressWarnings("deprecation")
				String charset = EntityUtils.getContentCharSet(entity) == null ? CHARSET_ENCODING
						: EntityUtils.getContentCharSet(entity);
				return new String(EntityUtils.toByteArray(entity), charset);
			} else {
				return null;
			}
		}
	};


	public static Integer TIMEOUT = 6000;

	/**
	 * 重写验证方法，取消检测ssl
	 */
	private static TrustManager trustManager = new X509TrustManager() {

		public void checkClientTrusted(java.security.cert.X509Certificate[] arg0, String arg1)
				throws CertificateException {
			// TODO Auto-generated method stub

		}

		public void checkServerTrusted(java.security.cert.X509Certificate[] arg0, String arg1)
				throws CertificateException {
			// TODO Auto-generated method stub

		}

		public java.security.cert.X509Certificate[] getAcceptedIssuers() {
			// TODO Auto-generated method stub
			return null;
		}

	};

	// private static String
	// USER_AGENT="Mozilla/4.0 (compatible; MSIE 6.0; Win32)";//ie6
	public static String USER_AGENT = "Mozilla/4.0 (compatible; MSIE 7.0; Win32)";// ie7

	/**
	 * 访问https的网站
	 * 
	 * @param httpclient
	 */
	@SuppressWarnings("deprecation")
	public static void enableSSL(DefaultHttpClient httpclient) {
		// 调用ssl
		try {
			SSLContext sslcontext = SSLContext.getInstance("TLS");
			sslcontext.init(null, new TrustManager[] { trustManager }, null);
			SSLSocketFactory sf = new SSLSocketFactory(sslcontext);
			sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
			Scheme https = new Scheme("https", sf, 443);
			httpclient.getConnectionManager().getSchemeRegistry().register(https);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 使用get方法获取相关的数据
	 * 
	 * @param url
	 * @param paramsList
	 * @return
	 */
	public static String get(String url, List<NameValuePair> paramsList) {
		return httpRequest(url, paramsList, "GET", CONTENT_TYPE_WWW_FORM, null, null);
	}

	/**
	 * 使用get方法并且通过代理获取相关的数据
	 * 
	 * @param url
	 * @param paramsList
	 * @param proxy
	 * @return
	 */
	public static String get(String url, List<NameValuePair> paramsList, HttpHost proxy) {
		return httpRequest(url, paramsList, "GET", CONTENT_TYPE_WWW_FORM, null, proxy);
	}

	/**
	 * 获取DefaultHttpClient对象
	 * 
	 * @param timeout
	 *
	 * @return DefaultHttpClient对象
	 */
	public static DefaultHttpClient getDefaultHttpClient(int timeout) {
		DefaultHttpClient httpclient = new DefaultHttpClient();
		// 模拟浏览器，解决一些服务器程序只允许浏览器访问的问题
		httpclient.getParams().setParameter(CoreProtocolPNames.USER_AGENT, USER_AGENT);
		httpclient.getParams().setParameter(CoreProtocolPNames.USE_EXPECT_CONTINUE, Boolean.FALSE);
		httpclient.getParams().setParameter(CoreProtocolPNames.HTTP_CONTENT_CHARSET, CHARSET_ENCODING);
		httpclient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, timeout);

		// 浏览器兼容性
		httpclient.getParams().setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.BROWSER_COMPATIBILITY);
		// 定义重试策略
		httpclient.setHttpRequestRetryHandler(requestRetryHandler);

		return httpclient;
	}

	/**
	 * 获取DefaultHttpClient对象
	 * 
	 * @param charset
	 *            字符编码
	 * @return DefaultHttpClient对象
	 */
	private static DefaultHttpClient getDefaultHttpClient(final String charset) {
		DefaultHttpClient httpclient = new DefaultHttpClient();
		// 模拟浏览器，解决一些服务器程序只允许浏览器访问的问题
		httpclient.getParams().setParameter(CoreProtocolPNames.USER_AGENT, USER_AGENT);
		httpclient.getParams().setParameter(CoreProtocolPNames.USE_EXPECT_CONTINUE, Boolean.FALSE);
		httpclient.getParams().setParameter(CoreProtocolPNames.HTTP_CONTENT_CHARSET,
				charset == null ? CHARSET_ENCODING : charset);
		if (TIMEOUT != null && TIMEOUT > 0) {
			httpclient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, TIMEOUT);
		}

		// 浏览器兼容性
		httpclient.getParams().setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.BROWSER_COMPATIBILITY);
		// 定义重试策略
		httpclient.setHttpRequestRetryHandler(requestRetryHandler);

		return httpclient;
	}

	/**
	 * 获取图片的字节数组
	 * 
	 * @createTime 2011-11-24
	 * @param url
	 * @return
	 * @throws IOException
	 * @throws ClientProtocolException
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static byte[] getImg(String url) throws ClientProtocolException, IOException {
		byte[] bytes = null;
		// 创建HttpClient实例
		DefaultHttpClient httpclient = getDefaultHttpClient(CHARSET_ENCODING);
		// 获取url里面的信息
		HttpGet hg = new HttpGet(url);
		HttpResponse hr = httpclient.execute(hg);
		bytes = EntityUtils.toByteArray(hr.getEntity());
		// 转换内容为字节
		return bytes;
	}

	/**
	 * 获取网络文件的字节数组
	 * 
	 * @param url
	 * @return
	 * @throws IOException
	 * @throws ClientProtocolException
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static byte[] getUrlFileBytes(String url) throws ClientProtocolException, IOException {
		byte[] bytes = null;
		// 创建HttpClient实例
		DefaultHttpClient httpclient = getDefaultHttpClient(CHARSET_ENCODING);
		// 获取url里面的信息
		HttpGet hg = new HttpGet(url);
		HttpResponse hr = httpclient.execute(hg);
		bytes = EntityUtils.toByteArray(hr.getEntity());
		// 转换内容为字节
		return bytes;
	}

	/**
	 * 提交数据到服务器
	 * 
	 * @param url
	 * @param fileMap
	 * @param stringMap
	 * @param type
	 * @param proxy
	 * @return
	 */
	public static String httpFileRequest(String url, Map<String, String> fileMap, Map<String, String> stringMap,
			int type, HttpHost proxy) {
		String responseStr = null;
		// 判断输入的值是是否为空
		if (null == url || "".equals(url)) {
			return null;
		}
		// 创建HttpClient实例
		DefaultHttpClient httpclient = getDefaultHttpClient(CHARSET_ENCODING);

		// 判断是否是https请求
		if (url.startsWith("https")) {
			enableSSL(httpclient);
		}

		// 如果代理对象不为空则设置代理
		if (null != proxy) {
			httpclient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
		}
		// 发送文件
		HttpPost hp = new HttpPost(url);
		MultipartEntity multiEntity = new MultipartEntity();
		try {
			// type=0是本地路径，否则是网络路径
			if (type == 0) {
				for (String key : fileMap.keySet()) {
					multiEntity.addPart(key, new FileBody(new File(fileMap.get(key))));
				}
			} else {
				for (String key : fileMap.keySet()) {
					multiEntity.addPart(key, new ByteArrayBody(getUrlFileBytes(fileMap.get(key)), key));
				}
			}
			// 加入相关参数 默认编码为utf-8
			for (String key : stringMap.keySet()) {
				multiEntity.addPart(key, new StringBody(stringMap.get(key), Charset.forName(CHARSET_ENCODING)));
			}
			hp.setEntity(multiEntity);
			responseStr = httpclient.execute(hp, responseHandler);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return responseStr;
	}

	/**
	 * 提交数据到服务器
	 * 
	 * @param url
	 * @param paramsList
	 * @param method
	 * @param proxy
	 * @return
	 */
	public static String httpRequest(String url, List<NameValuePair> paramsList, String method, String contentType,
			List<Header> headers, HttpHost proxy) {
		String responseStr = null;
		// 判断输入的值是是否为空
		if (null == url || "".equals(url)) {
			return null;
		}

		// 创建HttpClient实例
		DefaultHttpClient httpclient = getDefaultHttpClient(CHARSET_ENCODING);

		// 判断是否是https请求
		if (url.startsWith("https")) {
			enableSSL(httpclient);
		}
		String formatParams = null;
		// 将参数进行utf-8编码
		if (null != paramsList && paramsList.size() > 0) {
			formatParams = URLEncodedUtils.format(paramsList, CHARSET_ENCODING);
		}
		// 如果代理对象不为空则设置代理
		if (null != proxy) {
			httpclient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
		}
		// 如果headers不为空，则添加headers
		if (headers != null) {
			httpclient.getParams().setParameter("http.default-headers", headers);
		}
		try {
			// 如果方法为Get
			if ("GET".equalsIgnoreCase(method)) {
				if (formatParams != null) {
					url = (url.indexOf("?")) < 0 ? (url + "?" + formatParams)
							: (url.substring(0, url.indexOf("?") + 1) + formatParams);
				}
				HttpGet hg = new HttpGet(url);
				responseStr = httpclient.execute(hg, responseHandler);

				// 如果方法为Post
			} else if ("POST".equalsIgnoreCase(method)) {
				HttpPost hp = new HttpPost(url);
				if (formatParams != null) {
					StringEntity entity = new StringEntity(formatParams);
					entity.setContentType(StringUtils.isEmpty(contentType) ? CONTENT_TYPE_WWW_FORM : contentType);
					hp.setEntity(entity);
				}

				responseStr = httpclient.execute(hp, responseHandler);
			}

		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return responseStr;
	}


	/**
	 * 使用post方法获取相关的数据
	 * 
	 * @param url
	 * @param paramsList
	 * @return
	 */
	public static String post(String url, List<NameValuePair> paramsList) {
		return httpRequest(url, paramsList, "POST", CONTENT_TYPE_WWW_FORM, null, null);
	}

	/**
	 * 使用post方法并且通过代理获取相关的数据
	 * 
	 * @param url
	 * @param paramsList
	 * @param proxy
	 * @return
	 */
	public static String post(String url, List<NameValuePair> paramsList, HttpHost proxy) {
		return httpRequest(url, paramsList, "POST", CONTENT_TYPE_WWW_FORM, null, proxy);
	}

	/**
	 * 使用post方法获取相关的数据
	 * 
	 * @param url
	 * @param
	 * @return
	 */
	public static String post(String url, List<NameValuePair> paramsList, List<Header> headers) {
		return httpRequest(url, paramsList, "POST", CONTENT_TYPE_WWW_FORM, headers, null);
	}

	/**
	 * 使用post方法获取相关的数据
	 * 
	 * @param url
	 * @param bodyText
	 * @return
	 */
	public static String post(String url, String bodyText) {
		return post(url, bodyText, null, null, null);
	}

	public static String post(String url, String bodyText, HttpHost proxy) {
		return post(url, bodyText, CHARSET_ENCODING, null, proxy);
	}

	/**
	 * 使用post方法获取相关的数据
	 * 
	 * @param url
	 * @param bodyText
	 * @return
	 */
	public static String post(String url, String bodyText, String contentType) {
		return post(url, bodyText, contentType, null, null);
	}

	/**
	 * 使用post方法获取相关的数据
	 * 
	 * @param url
	 * @param bodyText
	 * @param proxy
	 * @return
	 */
	public static String post(String url, String bodyText, String contentType, List<Header> headers, HttpHost proxy) {
		String responseStr = null;
		// 判断输入的值是是否为空
		if (null == url || "".equals(url)) {
			return null;
		}

		// 创建HttpClient实例
		DefaultHttpClient httpclient = getDefaultHttpClient(CHARSET_ENCODING);

		// 判断是否是https请求
		if (url.startsWith("https")) {
			enableSSL(httpclient);
		}

		// 如果代理对象不为空则设置代理
		if (null != proxy) {
			httpclient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
		}
		// 如果headers不为空，则添加headers
		if (headers != null) {
			httpclient.getParams().setParameter("http.default-headers", headers);
		}
		try {
			HttpPost hp = new HttpPost(url);
			if (bodyText != null) {
				StringEntity entity = new StringEntity(bodyText, CHARSET_ENCODING);
				entity.setContentType(contentType != null && contentType.length() > 0 ? contentType : CONTENT_TYPE_WWW_FORM);
				hp.setEntity(entity);
			}
			responseStr = httpclient.execute(hp, responseHandler);

		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return responseStr;
	}

	/**
	 * 将相关文件和参数提交到相关服务器
	 * 
	 * @param url
	 * @param fileMap
	 * @param stringMap
	 * @return
	 */
	public static String postFile(String url, Map<String, String> fileMap, Map<String, String> stringMap) {
		return httpFileRequest(url, fileMap, stringMap, 0, null);
	}

	/**
	 * 将相关文件和参数提交到相关服务器
	 * 
	 * @param url
	 * @param urlMap
	 * @param stringMap
	 * @return
	 */
	public static String postUrlFile(String url, Map<String, String> urlMap, Map<String, String> stringMap) {
		return httpFileRequest(url, urlMap, stringMap, 1, null);
	}

}
