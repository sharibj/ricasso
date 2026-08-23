# Use a minimal base image
   FROM ubuntu:22.04

   # Install system dependencies
   RUN apt-get update && apt-get install -y \
       curl \
       wget \
       gnupg \
       git \
       xterm \
       && rm -rf /var/lib/apt/lists/*

   # Set TERM environment variable (required by the installer)
   ENV TERM=xterm

   # Install Node.js and npm (required by the installer)
   RUN curl -fsSL https://deb.nodesource.com/setup_22.x | bash -
   RUN apt-get install -y nodejs

   # Install Pi agent harness
   RUN curl -fsSL https://pi.dev/install.sh | sh

   # Create directory for Pi agent configuration
   RUN mkdir -p /root/.pi/agent

   # Copy your models.json file into the container
   COPY models.json /root/.pi/agent/models.json
   COPY APPEND_SYSTEM.md ~/.pi/agent/APPEND_SYSTEM.md

   # Expose the Pi agent's default port (if needed)
   EXPOSE 8000

   # Set working directory    
   WORKDIR /app

   # Setup pi    
   RUN pi install npm:pi-web-access
   RUN pi install npm:pi-subagents
   RUN pi install npm:pi-mcp-adapter

   # Run the Pi agent with Ollama configuration
   CMD ["pi", "-p", "Write a haiku.md with a small haiku about midnight"]


   # Keep container running
#    CMD ["sh", "-c", "tail -f /dev/null"]

# Shell Commands
# docker build -t pi-agent . 
# docker run --network host -v $(pwd):/app --name pi pi-agent
# or detached
# docker run --network host -d --name pi pi-agent
# docker save pi-agent:latest > pi-agent.tar