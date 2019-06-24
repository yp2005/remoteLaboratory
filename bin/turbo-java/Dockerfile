FROM openjdk:8
RUN mkdir -p /usr/share/fonts
ADD simsun.ttc /usr/share/fonts/
RUN cd /usr/share/fonts && chmod a+r simsun.ttc && fc-cache -f
RUN echo "Asia/Shanghai" > /etc/timezone;
add wait-for.sh /wait-for.sh
RUN chmod 755 /wait-for.sh
RUN apt-get update && apt-get install  netcat  -y