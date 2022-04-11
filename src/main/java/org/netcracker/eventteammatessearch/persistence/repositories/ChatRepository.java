package org.netcracker.eventteammatessearch.persistence.repositories;

import org.netcracker.eventteammatessearch.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {
    @Query("SELECT  c from  Chat c inner join ChatUser  cu on cu.chat=c where cu.user.login=:chatUser and c.isPrivate=true ")
    public Chat getByPrivateTrueAndChatUsersContains(String chatUser);

    public Chat getByEvent_Id(long id);

    @Query("SELECT  c from  Chat c  where EXISTS(SELECT cu from ChatUser cu where cu.user.login=:chatUser and cu.chat.id=:chatId) and c.id=:chatId")
    public Chat getByChatUsersContains(String chatUser, long chatId);

    @Query("SELECT  c from  Chat c  where EXISTS(SELECT cu from ChatUser cu where cu.user.login=:chatUser and cu.chat=c)")
    public List<Chat> getAllByChatUsersContains(String chatUser);
}
