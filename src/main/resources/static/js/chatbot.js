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

    // Afficher/masquer le chatbot
    toggleBtn.addEventListener('click', function() {
        const isOpen = chatbot.style.display === 'block';
        chatbot.style.display = isOpen ? 'none' : 'block';
        if (!isOpen) input.focus();
    });
    closeBtn.addEventListener('click', function() {
        chatbot.style.display = 'none';
    });

    // Envoyer un message
    function sendMessage() {
        const text = input.value.trim();
        if (!text) return;
        // Message utilisateur
        appendMessage('user-msg', text);
        input.value = '';
        // Réponse automatique (simulée)
        setTimeout(() => {
            let response = "I'm here to help! You can ask about tasks, status, or deadlines.";
            if (text.toLowerCase().includes('hello') || text.toLowerCase().includes('hi')) {
                response = "Hello! How can I assist you with your tasks today?";
            } else if (text.toLowerCase().includes('status')) {
                response = "You can view task statuses in the dashboard. Currently, we have tasks in progress, completed, and suspended.";
            } else if (text.toLowerCase().includes('deadline')) {
                response = "Deadlines are displayed in the task list. You can sort by completion date.";
            }
            appendMessage('bot-msg', response);
        }, 500);
    }

    function appendMessage(cls, text) {
        const div = document.createElement('div');
        div.className = cls;
        div.textContent = text;
        messages.appendChild(div);
        messages.scrollTop = messages.scrollHeight;
    }

    sendBtn.addEventListener('click', sendMessage);
    input.addEventListener('keypress', function(e) {
        if (e.key === 'Enter') sendMessage();
    });

    // Message de bienvenue
    setTimeout(() => {
        appendMessage('bot-msg', "👋 Hi! I'm your task assistant. Ask me anything about your tasks.");
    }, 1000);
});