package com.localmesh.storage;

import com.localmesh.model.Message;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MessageStorage {
	private final List<Message> messages = Collections.synchronizedList(new ArrayList<>());
	private final int maxCapacity;

	public MessageStorage() {
		this(1000); // по умолчанию храним последние 1000 сообщений
	}

	public MessageStorage(int maxCapacity) {
		this.maxCapacity = maxCapacity;
	}

	public void addMessage(Message msg) {
		synchronized (messages) {
			messages.add(msg);
			if (messages.size() > maxCapacity) {
				messages.remove(0); //удаление самых старых
			}
		}
	}

	public List<Message> getHistory() {
		synchronized (messages) {
			return new ArrayList<>(messages);
		}
	}


	public void clear() {
		messages.clear();
	}
}