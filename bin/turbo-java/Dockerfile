FROM iron/base
RUN echo '@edge http://nl.alpinelinux.org/alpine/edge/main' >> /etc/apk/repositories \
  && echo '@community http://nl.alpinelinux.org/alpine/edge/community' >> /etc/apk/repositories \
  && apk update \
  && apk upgrade \
  && apk add openjdk8-jre@community fontconfig ttf-dejavu font-adobe-100dpi\
  && rm -rf /var/cache/apk/*
RUN mkdir -p /usr/share/fonts
ADD simsun.ttc /usr/share/fonts/
RUN cd /usr/share/fonts && chmod a+r simsun.ttc && fc-cache -f
RUN echo "Asia/Shanghai" > /etc/timezone;
add wait-for.sh /wait-for.sh
RUN chmod 755 /wait-for.sh