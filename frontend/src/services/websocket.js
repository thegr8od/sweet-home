export class ChatWebSocket {
  constructor() {
    this.ws = null
    this.url = 'ws://localhost:8080/chat'
  }

  connect() {
    this.ws = new WebSocket(this.url)

    this.ws.onopen = () => {
      console.log('WebSocket Connected')
    }

    this.ws.onmessage = (event) => {
      const message = JSON.parse(event.data)
      // 메시지 처리 로직
    }
  }

  sendMessage(message) {
    if (this.ws && this.ws.readyState === WebSocket.OPEN) {
      this.ws.send(JSON.stringify(message))
    }
  }
}
