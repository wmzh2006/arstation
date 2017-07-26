package com.funoble.myarstation.common;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Vector;

import com.funoble.myarstation.game.MyArStation;

import android.content.Context;

/**
 * 用户账户信息文件存储
 */
public class IMEIStore
{
	String path = "/lyingdice/.cache/.tmp/";
	File sdCard = android.os.Environment.getExternalStorageDirectory();
	String dir = sdCard.getPath()+path;
	   
	/**
	 *最大历史记录条数
	 */
	private static final int	MAX_ACCOUNT			= 1;

	/**
	 * 存储账号的文件的名字
	 */
	private static final String	ACCOUNT_STORE_NAME	= "Lyndice";

	/**
	 * 上下文环境
	 */
	private Context				context;

	/**
	 * 保存账户信息的文件名
	 */
	private String				fileName;

	/**
	 * 最多存多少条记录
	 */
	private final int			maxSize;

	/**
	 * 存储用户信息的列表
	 */
	private Vector<AccountInfo>	accountList;

	/**
	 * 构造函数
	 * 
	 * @param context
	 */
	public IMEIStore(Context context)
	{
		this.context = context.getApplicationContext();
		this.fileName = ACCOUNT_STORE_NAME;
		this.maxSize = MAX_ACCOUNT;
		if (accountList == null)
			accountList = new Vector<AccountInfo>();
	}

	/**
	 * 打开文件存储，读取账户信息
	 * 
	 * @param fileName
	 */
	public void open()
	{
		Tools.debug("IMEIStore::open() start");
		FileInputStream in = null;
		DataInputStream din = null;
		try
		{
			File file = new File(dir);
			if(!file.exists()) {
				file.mkdirs();
			}
			File file1 = new File(dir+fileName);
			if(!file1.exists()) {
				file1.createNewFile();
			}
			in = new FileInputStream(file1);
			din = new DataInputStream(in);
			readStream(din);
			in.close();
			din.close();
			Tools.debug("IMEIStore::open() end");
		}
		catch (Exception e)
		{
			Tools.debug("IMEIStore::open() error1!!!");
			// Toast
			// .makeText(context, "AccountStore open error:" + e.toString(),
			// Toast.LENGTH_LONG)
			// .show();
			e.printStackTrace();
			try
			{
				if (in != null)
				{
					in.close();
					in = null;
				}

				if (din != null)
				{
					din.close();
					din = null;
				}
			}
			catch (Exception ee)
			{
				Tools.debug("IMEIStore::open() error2!!!");
			}
		}
	}

	/**
	 * 获取账号数
	 * 
	 * @return
	 */
	public int getAccountSize()
	{
		if (accountList == null)
			return 0;
		return accountList.size();
	}

	/**
	 * 将用户账户信息保存到文件
	 */
	public void commit()
	{
		FileOutputStream out = null;
		DataOutputStream dout = null;
		try
		{
			Tools.debug("IMEIStore::commit() start");
			File file = new File(dir);
			if(!file.exists()) {
				file.mkdirs();
			}
			File file1 = new File(dir+fileName);
			out = new FileOutputStream(file1);
			dout = new DataOutputStream(out);
			writeStream(dout);
			out.close();
			dout.close();
			Tools.debug("IMEIStore::commit() end");
		}
		catch (Exception e)
		{
			Tools.debug("IMEIStore::commit() error1!!!");
			// Toast
			// .makeText(context, "AccountStore commit error:" + e.toString(),
			// Toast.LENGTH_LONG)
			// .show();
			e.printStackTrace();
		}
		finally
		{
			Tools.debug("IMEIStore::commit() error2!!!");
			try
			{
				if (out != null)
				{
					out.close();
					out = null;
				}

				if (dout != null)
				{
					dout.close();
					dout = null;
				}
			}
			catch (Exception ee)
			{
				Tools.debug("IMEIStore::commit() error3!!!");
			}
		}
	}

	/**
	 * 关闭文件存储
	 */
	public void close()
	{
		if (accountList != null)
			accountList.clear();
	}

	/**
	 * 将账户信息放入列表头位置
	 * 如果是已有账户则提到列表头,
	 * 或者更新历史记录中第一个元素的信息
	 * 
	 * @param info
	 */
	public void pushAndUpdate(AccountInfo _info)
	{
		if (_info == null || _info.getName().equals(""))
			return;

		AccountInfo info = new AccountInfo();
		info.copy(_info);

		int index = search(info);
		if (index >= 0)
		{
			accountList.remove(index);
		}
		accountList.insertElementAt(info, 0);
		if (accountList.size() > maxSize)
			accountList.remove(accountList.lastElement());
	}

	/**
	 * 获取列表头位置的账户信息
	 * 
	 * @return
	 */
	public AccountInfo peek()
	{
		if (accountList.size() == 0)
			return null;
		return accountList.elementAt(0);
	}

	/**
	 * 删除指定位置的账户信息
	 * 
	 * @param index
	 */
	public void remove(String name)
	{
		int index = -1;
		for (int i = 0; i < accountList.size(); i++)
		{
			if (accountList.elementAt(i).getName().equals(name))
			{
				index = i;
				break;
			}
		}
		accountList.remove(index);
	}

	public AccountInfo get(String name)
	{
		for (int i = 0; i < accountList.size(); i++)
		{
			if (accountList.elementAt(i).getName().equals(name))
			{
				return accountList.elementAt(i);
			}
		}
		return null;
	}

	/**
	 * 根据索引位置获取帐户对象
	 * 
	 * @param index
	 * @return
	 */
	private AccountInfo get(int index)
	{
		if (index >= 0 && index < accountList.size())
		{
			return accountList.get(index);
		}

		return null;
	}

	/**
	 * 搜索指定账户的位置
	 * 
	 * @param name
	 * @return
	 */
	public int search(AccountInfo info)
	{
		if (info == null)
			return -1;

		for (int i = 0; i < accountList.size(); i++)
		{
			if (accountList.elementAt(i).getName().equals(info.getName()))
				return i;
		}
		return -1;
	}

	/**
	 * 搜索指定账户的位置
	 * 
	 * @param name
	 * @return
	 */
	public int search(String name)
	{
		if (name == null || name.equals(""))
			return -1;

		for (int i = 0; i < accountList.size(); i++)
		{
			if (accountList.elementAt(i).getName().equals(name))
				return i;
		}
		return -1;
	}

	/**
	 * 搜索帐户对象
	 * 
	 * @param name
	 * @return
	 */
	public AccountInfo searchV2(String name)
	{
		return get(search(name));
	}


	/**
	 * 是否有账户信息
	 * 
	 * @return
	 */
	public boolean isEmpty()
	{
		return (accountList.size() == 0);
	}

	/**
	 * 账户转换成字符串数组
	 * 
	 * @return
	 */
	public String[] toAccountArray()
	{
		String[] accounts = new String[accountList.size()];
		for (int i = 0; i < accounts.length; i++)
		{
			accounts[i] = accountList.elementAt(i).getName();
		}
		return accounts;
	}

	/**
	 * 将所有用户账户信息写入流
	 * 
	 * @param dout
	 * @throws Exception
	 */
	private void writeStream(DataOutputStream dout) throws Exception
	{
		dout.writeByte(accountList.size());
		for (int i = 0; i < accountList.size(); i++)
		{
			AccountInfo info = accountList.elementAt(i);
			info.writeStream(dout);
		}
		dout.flush();
	}

	/**
	 * 将所有用户信息读入流
	 * 
	 * @param din
	 * @throws Exception
	 */
	private void readStream(DataInputStream din) throws Exception
	{
		accountList.clear();
		byte size = din.readByte();
		for (int i = 0; i < size; i++)
		{
			AccountInfo info = new AccountInfo();
			info.readStream(din);
			accountList.add(info);
		}
	}

	/**
	 * 用户账户信息类
	 * 
	 * @author chriszheng
	 */
	public static class AccountInfo
	{
		/**
		 * 用户账户
		 */
		private String	name;

		/**
		 * 用户当前输入的密码（2次md5加密）
		 */
		private byte[]	inputPwd;

		/**
		 * 用户当前输入的密码（1次md5加密）
		 */
		private byte[]	inputPwdOne;

		/**
		 * 登录成功的密码（2次md5加密）
		 */
		private byte[]	succeedPwd;

		/**
		 * 用户当前输入的密码（1次md5加密）
		 */
		private byte[]	succeedPwdOne;

		/**
		 * 密码长度
		 */
		private byte	lengthOfPwd;


		/**
		 * 是否设置了保存密码
		 */
		private boolean	isSavedPwd;

		/**
		 * 是否设置了自动登录
		 */
		private boolean	isAutoLogin;

		/**
		 * 是否快速游戏
		 */
		private boolean	isLoginPlay;
		/**
		 * 是否已经成功登录
		 */
		private boolean	isLoginSucc;

		/**
		 * 游客
		 */
		private boolean isVisitor;
		
		/**
		 * 渠道号
		 */
		private int channelID;
		
		/**
     * 
     */
		public AccountInfo()
		{
			restore();
		}

		/**
		 * 拷贝指定对象的值到该对象
		 * 
		 * @param info
		 */
		public void copy(AccountInfo info)
		{
			if (info == null || info == this)
				return;
			name = new String(info.getName());
			inputPwd = info.getInputPwd();
			inputPwdOne = info.getInputPwdOne();
			succeedPwd = info.getSucceedPwd();
			succeedPwdOne = info.getSucceedPwdOne();
			lengthOfPwd = info.getPwdLength();
			isSavedPwd = info.isSavedPwd;
			isAutoLogin = info.isAutoLogin;
			isLoginPlay = info.isLoginPlay;
			isLoginSucc = info.isLoginSucc;
			isVisitor = info.isVisitor;
			channelID = info.channelID;
		}

		/**
		 * 恢复到默认状态
		 */
		public void restore()
		{
			name = "";
			inputPwd = new byte[0];
			inputPwdOne = new byte[0];
			succeedPwd = new byte[0];
			succeedPwdOne = new byte[0];
			lengthOfPwd = 0;
			isSavedPwd = true;
			isAutoLogin = false;
			isLoginPlay = true;
			isLoginSucc = false;
			isVisitor = true;
			channelID = 0;
		}

		/**
		 * 设置账户信息
		 * 
		 * @param name
		 * @return
		 */
		public AccountInfo setName(String name)
		{
			this.name = name;
			return this;
		}

		/**
		 * 获取账户信息
		 * 
		 * @return
		 */
		public String getName()
		{
			return this.name;
		}

		/**
		 * 该用户是否已经成功登录
		 * 
		 * @return
		 */
		public boolean isLoginSucc()
		{
			return isLoginSucc;
		}

		/**
		 * 设置是否已经成功登录
		 * @param is
		 * @return
		 */
		public AccountInfo setIsLoginSucc(boolean is)
		{
			this.isLoginSucc = is;
			return this;
		}

		/**
		 * 设置当前输入密码
		 * 
		 * @param pwd
		 * @return
		 */
		public AccountInfo setInputPwd(byte[] pwd)
		{
			this.inputPwd = pwd;
			return this;
		}

		/**
		 * 
		 * @param pwd
		 * @return
		 */
		public AccountInfo setInputPwdOne(byte[] pwd)
		{
			this.inputPwdOne = pwd;
			return this;
		}

		/**
		 * 获取当前输入密码
		 * 
		 * @return
		 */
		public byte[] getInputPwd()
		{
			return this.inputPwd;
		}

		/**
		 * 获取当前输入密码
		 * 
		 * @return
		 */
		public byte[] getInputPwdOne()
		{
			return this.inputPwdOne;
		}

		/**
		 * 设置成功登录的密码
		 * 
		 * @param pwd
		 * @return
		 */
		public AccountInfo setSucceedPwd(byte[] pwd)
		{
			this.succeedPwd = pwd;
			return this;
		}

		/**
		 * 设置成功登录的密码
		 * 
		 * @param pwd
		 * @return
		 */
		public AccountInfo setSucceedPwdOne(byte[] pwd)
		{
			this.succeedPwdOne = pwd;
			return this;
		}

		/**
		 * 获取成功登录的密码
		 * 
		 * @return
		 */
		public byte[] getSucceedPwd()
		{
			return this.succeedPwd;
		}

		/**
		 * 获取成功登录的密码
		 * 
		 * @return
		 */
		public byte[] getSucceedPwdOne()
		{
			return this.succeedPwdOne;
		}

		/**
		 * 设置是否保存密码
		 * 
		 * @param isSavedPwd
		 * @return
		 */
		public AccountInfo setSavedPwd(boolean isSavedPwd)
		{
			this.isSavedPwd = isSavedPwd;
			return this;
		}

		/**
		 * 是否保存密码
		 * 
		 * @return
		 */
		public boolean isSavedPwd()
		{
			return this.isSavedPwd;
		}

		/**
		 * 设置密码长度
		 * 
		 * @param len
		 */
		public void setPwdLength(byte len)
		{
			this.lengthOfPwd = len;
		}

		/**
		 * 取得密码的长度
		 * 
		 * @return
		 */
		public byte getPwdLength()
		{
			return this.lengthOfPwd;
		}

		/**
		 * 设置是否自动登录
		 * 
		 * @param isAutoLogin
		 * @return
		 */
		public AccountInfo setAutoLogin(boolean isAutoLogin)
		{
			this.isAutoLogin = isAutoLogin;
			return this;
		}

		/**
		 * 是否自动登录
		 * 
		 * @return
		 */
		public boolean isAutoLogin()
		{
			return this.isAutoLogin;
		}

		/**
		 * 是否快速游戏
		 */
		public boolean isLoginPlay()
		{
			return this.isLoginPlay;
		}

		/**
		 * 设置是否登录并快速游戏
		 * 
		 * @param isAutoLogin
		 * @return
		 */
		public AccountInfo setLoginPlay(boolean isLoginPlay)
		{
			this.isLoginPlay = isLoginPlay;
			return this;
		}

		public boolean isVisitor() {
			return this.isVisitor;
		}
		
		public void setVisitor(boolean isVisitor) {
			this.isVisitor = isVisitor;
		}
		
		public void setChannel(int channID) {
			this.channelID = channID;
		}
		
		public int getChannel() {
			return this.channelID;
		}
		/**
		 * 将用户账户信息写入流
		 * 
		 * @param dout
		 * @throws Exception
		 */
		public void writeStream(DataOutputStream dout) throws Exception
		{
			dout.writeUTF(this.name);

			dout.writeByte(inputPwd.length);
			dout.write(inputPwd);

			dout.writeByte(inputPwdOne.length);
			dout.write(this.inputPwdOne);

			dout.writeByte(succeedPwd.length);
			dout.write(succeedPwd);

			dout.writeByte(succeedPwdOne.length);
			dout.write(succeedPwdOne);

			dout.writeByte(this.lengthOfPwd);
			dout.writeBoolean(this.isSavedPwd);
			dout.writeBoolean(this.isAutoLogin);
			dout.writeBoolean(this.isLoginPlay);
			dout.writeBoolean(this.isVisitor);
			dout.writeInt(this.channelID);
			dout.flush();
		}

		/**
		 * 从流中读取用户信息
		 * 
		 * @param din
		 * @throws Exception
		 */
		public void readStream(DataInputStream din)
		{
			try
			{
				this.name = din.readUTF();

				int size = din.readByte();
				inputPwd = new byte[size];
				din.read(inputPwd);

				size = din.readByte();
				inputPwdOne = new byte[size];
				din.read(inputPwdOne);

				size = din.readByte();
				succeedPwd = new byte[size];
				din.read(succeedPwd);

				size = din.readByte();
				succeedPwdOne = new byte[size];
				din.read(succeedPwdOne);

				this.lengthOfPwd = din.readByte();
				this.isSavedPwd = din.readBoolean();
				this.isAutoLogin = din.readBoolean();
				this.isLoginPlay = din.readBoolean();
				this.isVisitor = din.readBoolean();
				this.channelID = din.readInt();
			}
			catch (Exception e)
			{
				Tools.debug("readStream exception. detail:" + e.toString());
			}

		}

		public String toString()
		{
			StringBuffer buffer = new StringBuffer();
			buffer.append("name=").append(this.name);
			buffer.append("\ninputPwd=").append(this.inputPwd);
			buffer.append("\nsucceedPwd=").append(this.succeedPwd);
			buffer.append("\nisSavedPwd=").append(this.isSavedPwd);
			buffer.append("\nisAutoLogin=").append(this.isAutoLogin);
			buffer.append("\nisLoginPlay=").append(this.isLoginPlay);
			buffer.append("\nisLoginSucc=").append(this.isLoginSucc);
			buffer.append("\nisVisitor=").append(this.isVisitor);
			return buffer.toString();
		}

	}
}
