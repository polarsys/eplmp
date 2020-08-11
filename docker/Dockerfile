FROM polarsys/eplmp-base:latest

COPY eplmp-server/eplmp-server-ear/target/eplmp-server-ear.ear $DEPLOY_DIR
COPY docker/entrypoint.sh /entrypoint.sh
COPY docker/asadmin.commands /asadmin.commands

USER root

RUN chmod +x /entrypoint.sh && \
 chown payara:payara /asadmin.commands

USER payara

ENTRYPOINT ["/entrypoint.sh"]
