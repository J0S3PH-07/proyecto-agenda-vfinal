// Lambda function to process reports from SQS
exports.handler = async (event) => {
    for (const record of event.Records) {
        const body = JSON.parse(record.body);
        console.log(`[ASYNC PROCESSOR] Iniciando generación de informe para: ${body.titulo || 'Sin título'}`);
        
        // Simular trabajo pesado (esperar 2 segundos)
        await new Promise(resolve => setTimeout(resolve, 2000));
        
        console.log(`[ASYNC PROCESSOR] Informe finalizado con éxito para ID: ${record.messageId}`);
    }
    return { statusCode: 200 };
};
