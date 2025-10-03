<template>
  <UsersList
    :title="`Member <span style='font-family: Arial Black, serif'>Profiles</span>`"
    :get-users="getUsers"
  />
</template>

<script>
import UserRepository from "@/repositories/UserRepository";
import UsersList from "@/components/users/UsersList";

export default {
  name: "UsersView.vue",
  components: { UsersList },
  methods: {
    async getUsers() {
      return await UserRepository.findActive();
    },
  },
};
</script>

<style scoped></style>
