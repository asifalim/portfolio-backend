import { Component, OnInit } from '@angular/core';
import {ChatMessage, ChatService} from "../../core/services/chat.service";

@Component({
  selector: 'app-chat',
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.scss']
})
export class ChatComponent implements OnInit {

  messages: ChatMessage[] = [
    { role: 'assistant', content: "Hey! I'm Alex's AI Agent. Ask me anything about Alex's skills, projects, or experience! 👋" }
  ];

  inputText = '';
  isTyping = false;

  suggestions = [
    "What's your tech stack?",
    "Tell me about your experience",
    "What projects have you built?",
    "Are you open to work?"
  ];

  constructor(private chatService: ChatService) {}

  ngOnInit(): void {
        console.log('ai chat open')
    }

  sendMessage(text?: string) {
    const message = text || this.inputText.trim();
    if (!message) return;

    this.messages.push({ role: 'user', content: message });
    this.inputText = '';
    this.isTyping = true;

    const history = this.messages.slice(0, -1).map(m => ({
      role: m.role as 'user' | 'assistant',
      content: m.content
    }));

    this.chatService.sendMessage({ message, history }).subscribe({
      next: (res) => {
        this.isTyping = false;
        if (res.success) {
          this.messages.push({ role: 'assistant', content: res.message });
        }
      },
      error: () => {
        this.isTyping = false;
        this.messages.push({
          role: 'assistant',
          content: "I'm having trouble connecting. Please try again!"
        });
      }
    });
  }

}
