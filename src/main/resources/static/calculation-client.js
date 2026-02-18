async function consumeFluxStream() {
    const response = await fetch('http://localhost:8083/api/stream-data');

    if (!response.body) {
        console.error('Поток недоступен');
        return;
    }

    const reader = response.body.getReader();
    const decoder = new TextDecoder();

    try {
        while (true) {
            const { done, value } = await reader.read();

            if (done) {
                break;
            }

            const chunk = decoder.decode(value, { stream: true });

        }
    } catch (error) {
        console.error('Ошибка чтения потока:', error);
    } finally {
        reader.releaseLock();
    }
}

consumeFluxStream();