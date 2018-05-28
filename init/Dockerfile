FROM fedora:28

RUN yum update -y
RUN yum install -y origin-clients

COPY create-keystore.sh /tmp

CMD /tmp/create-keystore.sh
