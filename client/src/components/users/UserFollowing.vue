<template>
  <div class="row">
    <h2>
      Following <span style="font-family: Arial Black, serif">Members</span>
    </h2>
  </div>
  <div class="row ps-3 pe-3">
    <div class="border" style="border-radius: 0 0 5px 5px">
      <UsersList :title="''" :get-users="getUserFollowing" />
    </div>
  </div>
</template>

<script>
import UserRepository from "@/repositories/UserRepository";
import { getStore } from "@/common/store";
import UsersList from "@/components/users/UsersList";

export default {
  name: "UserFollowing",
  components: { UsersList },
  props: {
    user: {
      type: Object,
      required: true,
    },
  },
  methods: {
    async getUserFollowing() {
      return await UserRepository.findUserFollowing(
        getStore().state.user.id,
        null,
        null
      );
    },
  },
};
</script>

<style scoped></style>
