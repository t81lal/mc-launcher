package eu.bibl.launcher.game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public final class ProcessMonitor extends Thread {
	
	private Process process;
	
	public ProcessMonitor(Process process) {
		this.process = process;
	}
	
	private boolean isRunning() {
		try {
			process.exitValue();
		} catch (IllegalThreadStateException e) {
			return true;
		}
		return false;
	}
	
	@Override
	public void run() {
		BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
		String line;
		while (isRunning()) {
			try {
				while ((line = reader.readLine()) != null) {
					System.out.println("[GAME]: " + line);
				}
			} catch (IOException e) {
			}
		}
		try {
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}