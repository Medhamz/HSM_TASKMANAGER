// ==================================================
// CHATBOT.JS - Assistant virtuel
// ==================================================
document.addEventListener('DOMContentLoaded', function() {
    const chatbot = document.getElementById('chatbot');
    const toggleBtn = document.getElementById('chatbotButton');
    const closeBtn = document.getElementById('chatbotClose');
    const sendBtn = document.getElementById('chatSend');
    const input = document.getElementById('chatInput');
    const messages = document.getElementById('chatMessages');

    if (!chatbot || !toggleBtn || !closeBtn || !sendBtn || !input || !messages) {
        console.error('❌ Chatbot: éléments introuvables dans le DOM.');
        return;
    }

    function toggleChat() {
        const isVisible = window.getComputedStyle(chatbot).display !== 'none';
        chatbot.style.display = isVisible ? 'none' : 'flex';
        if (!isVisible) {
            input.focus();
            if (messages.children.length === 0) {
                appendMessage('bot-msg', "👋 Hi! I'm your task assistant. Ask me anything about your tasks.");
            }
        }
    }

    toggleBtn.addEventListener('click', toggleChat);
    closeBtn.addEventListener('click', function(e) {
        e.stopPropagation();
        chatbot.style.display = 'none';
    });

    function appendMessage(cls, text) {
        const div = document.createElement('div');
        div.className = cls;
        div.textContent = text;
        messages.appendChild(div);
        messages.scrollTop = messages.scrollHeight;
    }

    function sendMessage() {
        const text = input.value.trim();
        if (!text) return;

        appendMessage('user-msg', text);
        input.value = '';

        setTimeout(() => {
            let response = "I'm here to help! You can ask about tasks, status, or deadlines.";
            const lower = text.toLowerCase();
            if (lower.includes('hello') || lower.includes('hi') || lower.includes('bonjour')) {
                response = "Hello! How can I assist you with your tasks today?";
            } else if (lower.includes('status') || lower.includes('etat')) {
                response = "You can view task statuses in the dashboard. Currently, we have tasks in progress, completed, and suspended.";
            } else if (lower.includes('deadline') || lower.includes('date')) {
                response = "Deadlines are displayed in the task list. You can sort by completion date.";
            } else if (lower.includes('project') || lower.includes('projet')) {
                response = "Projects are listed in the Projects tab. Each project contains multiple test classes.";
            } else if (lower.includes('help') || lower.includes('aide')) {
                response = "I can help with: tasks, projects, statuses, deadlines. Just ask!";
            }
            appendMessage('bot-msg', response);
        }, 500);
    }

    sendBtn.addEventListener('click', sendMessage);
    input.addEventListener('keypress', function(e) {
        if (e.key === 'Enter') {
            e.preventDefault();
            sendMessage();
        }
    });

    console.log('✅ Chatbot initialisé.');
});