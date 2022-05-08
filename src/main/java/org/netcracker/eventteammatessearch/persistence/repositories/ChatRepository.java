package org.netcracker.eventteammatessearch.persistence.repositories;

import org.netcracker.eventteammatessearch.entity.Chat;
import org.netcracker.eventteammatessearch.entity.ChatUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {
    @Query("SELECT  c from  Chat c  where (select  COUNT(cu) from ChatUser cu where (cu.user.login=:chatUser or cu.user.login=:callerLogin) and cu.chat=c)=2  and c.isPrivate=true ")
    public Chat getByPrivateTrueAndChatUsersContains(String chatUser, String callerLogin);

    public Chat getByEvent_Id(long id);



    @Query("SELECT  c from  Chat c  where EXISTS(SELECT cu from ChatUser cu where cu.user.login=:chatUser and cu.chat.id=:chatId) and c.id=:chatId")
    public Chat getByChatUsersContains(String chatUser, long chatId);

    @Query("SELECT  c from  Chat c  where EXISTS(SELECT cu from ChatUser cu where cu.user.login=:chatUser and cu.chat=c)")
    public List<Chat> getAllByChatUsersContains(String chatUser);

    @Query("select c from Chat c where c.isPrivate=true and  (select count (cu) from ChatUser cu where cu.id.chatId=c.id and cu.id.userId in ?1)>1")
    public Chat findChatByPrivateTrueAndChatUsers(Set<String> chatUsers);


}
